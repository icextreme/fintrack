package ca.sfu.iat.fintrack

import android.content.ContentValues
import android.util.Log
import ca.sfu.iat.fintrack.model.Budget
import ca.sfu.iat.fintrack.model.Record
import ca.sfu.iat.fintrack.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FirebaseHandler {
    private var database: DatabaseReference = Firebase.database.reference
    private var users = database.child("users")

    fun writeNewUser(user: User, uid: String) {
        users.child(uid).get().addOnSuccessListener {
            if (it.value == null) {
                database.child("users").child(uid).setValue(user)
            }

            writeNewBudget(uid, "Expenses", 1000.00)

            Log.i("firebase", "Successfully added $it")
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun writeEntry(
        userId: String,
        budgetId: String,
        recordName: String,
        amount: Double,
        category: String,
        date: String,
        type: String
    ) {
        val key = users.child(userId).child("budgets").child(budgetId)
            .child("records")
            .push().key
        if (key == null) {
            Log.w(ContentValues.TAG, "Couldn't get push key for posts")
            return
        }
        val record = Record(recordName, amount, category, type)
        record.date = date
        val postRecord = record.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/users/$userId/budgets/$budgetId/records/$key" to postRecord
        )
        database.updateChildren(childUpdates)
    }


    private fun writeNewBudget(
        userId: String,
        budgetName: String,
        startingAmount: Double,
    ) {
        val key = database.child("users").child(userId).child("budgets").push().key

        if (key == null) {
            Log.w("FirebaseHandler.writeNewBudget", "Couldn't get push key for posts")
            return
        }

        val budget = Budget(budgetName, startingAmount)
        val postBudget = budget.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/users/$userId/budgets/$key" to postBudget
        )

        database.updateChildren(childUpdates).addOnCompleteListener {
            Log.i("FirebaseHandler.writeNewBudget", "Added new budget")
        }.addOnFailureListener {
            Log.e("FirebaseHandler.writeNewBudget", "Failed to add new budget")
        }
    }
}
