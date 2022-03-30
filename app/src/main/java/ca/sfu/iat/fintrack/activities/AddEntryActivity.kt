package ca.sfu.iat.fintrack.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.databinding.ActivityAddEntryBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import ca.sfu.iat.fintrack.FirebaseHandler
import ca.sfu.iat.fintrack.databinding.ActivityMainBinding
import ca.sfu.iat.fintrack.model.Type
import java.time.LocalDate

class AddEntryActivity : AppCompatActivity() {
    val dbHandler = FirebaseHandler()
    private lateinit var binding: ActivityAddEntryBinding
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.buttonAddEntry.setOnClickListener {
            val date = binding.dateDisplay.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val itemName = binding.billItem.text.toString()
            val price = binding.priceId.text.toString().toDouble()
            val type: String = findViewById<View?>(binding.toggleButtonGroup.checkedButtonId).tag.toString()
            dbHandler.writeEntry("abcd123", itemName, price, category, date, type)
            setContentView(binding.root)
        }
    }

    private fun updateEditTextView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.dateDisplay.setText(sdf.format(calendar.time))
    }
}