package ca.sfu.iat.fintrack.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import ca.sfu.iat.fintrack.FirebaseHandler
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.databinding.ActivityAddEntryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class AddEntryActivity : AppCompatActivity() {
    private val dbHandler = FirebaseHandler()
    private lateinit var database: DatabaseReference

    private lateinit var binding: ActivityAddEntryBinding
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val budgetSpinner = binding.spinnerBudget
        database = Firebase.database.reference

        val budgets = hashMapOf<String, String>()

        FirebaseAuth.getInstance().currentUser?.let { it1 ->
            database.child("users").child(it1.uid).child("budgets").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (each in snapshot.children) {
                            val budgetId = each.key.toString()
                            Log.w("FirebaseAuth.getInstance.budgets", budgetId)
                            val budgetName = each.child("name").getValue<String>()
                            if (budgetName != null) {
                                Log.w("FirebaseAuth.getInstance.budgets", budgetName)
                                budgets[budgetName] = budgetId
                            }
                        }
                        setupSpinner(budgets, budgetSpinner)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(
                            "FirebaseAuth.getInstance.budgets.onCancelled",
                            "error",
                            error.toException()
                        )
                    }
                }
            )
        }

//        ArrayAdapter.createFromResource(
//            this,
//            R.array.budget_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            budgetSpinner.adapter = adapter
//        }

        val spinner = binding.spinnerCategory
        ArrayAdapter.createFromResource(
            this,
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateEditTextView()
                Log.d("DEBUG:", "year: $year month: $month day: $day")
            }

        binding.dateDisplay.setOnClickListener {
            DatePickerDialog(
                this@AddEntryActivity,
                R.style.datePicker,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.buttonAddEntry.setOnClickListener {
            val date = binding.dateDisplay.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val itemName = binding.billItem.text.toString()
            val price = binding.priceId.text.toString().toDouble()
            val type: String =
                findViewById<View?>(binding.toggleButtonGroup.checkedButtonId).tag.toString()

            FirebaseAuth.getInstance().currentUser?.let { it1 ->
                budgets[budgetSpinner.selectedItem.toString()]?.let { it2 ->
                    dbHandler.writeEntry(
                        it1.uid,
                        it2,
                        itemName,
                        price,
                        category,
                        date,
                        type
                    )
                }
            }

            clearFields()
        }

        binding.buttonDone.setOnClickListener {
            clearFields()
//            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }


    private fun setupSpinner(budgets: HashMap<String, String>, budgetSpinner: Spinner) {
        val budgetNames: List<String> = budgets.keys.toList()
        Log.d("budgets", budgets.size.toString())
        Log.d("budgetNames", budgetNames.size.toString())

        val spinnerArrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, budgetNames)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        budgetSpinner.adapter = spinnerArrayAdapter
   
    }
    
    private fun clearFields() {
        binding.dateDisplay.setText("")
        binding.billItem.setText("")
        binding.priceId.setText("")
        binding.dateDisplay.setText("")
    }
    private fun updateEditTextView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.dateDisplay.setText(sdf.format(calendar.time))
    }
}