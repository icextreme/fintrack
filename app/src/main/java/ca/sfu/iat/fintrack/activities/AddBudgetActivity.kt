package ca.sfu.iat.fintrack.activities

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.databinding.ActivityAddBudgetBinding
import ca.sfu.iat.fintrack.model.Budget
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddBudgetActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityAddBudgetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_budget)
        database = Firebase.database.reference

        binding = ActivityAddBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCreate.setOnClickListener {
            finish()
        }

        val periodSpinner = binding.spinnerPeriod

        ArrayAdapter.createFromResource(
            this,
            R.array.period_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            periodSpinner.adapter = adapter
        }

        binding.buttonCreate.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.let { it1 ->
                writeNewBudget(
                    it1.uid,
                    binding.budgetNameEditText.text.toString(),
                    binding.startingBudgetEditText.text.toString().toDouble(),
                    binding.spinnerPeriod.selectedItem.toString()
                )
            }
            finish()
        }
    }

    private fun writeNewBudget(
        userId: String,
        budgetName: String,
        startingAmount: Double,
        period: String
    ) {
        val key = database.child("users").child(userId).child("budgets").push().key

        if (key == null) {
            Log.w("FirebaseHandler.writeNewBudget", "Couldn't get push key for posts")
            return
        }

        val budget = Budget(budgetName, startingAmount, period)
        val postBudget = budget.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/users/$userId/budgets/$key" to postBudget
        )

        database.updateChildren(childUpdates).addOnCompleteListener {
            Log.i("FirebaseHandler.writeNewBudget", "Added new budget")
            // Exclusive for this activity
            finish()
        }.addOnFailureListener {
            Log.e("FirebaseHandler.writeNewBudget", "Failed to add new budget")
        }
    }
}