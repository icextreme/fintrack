package ca.sfu.iat.fintrack

import android.content.ContentValues
import android.util.Log
import ca.sfu.iat.fintrack.model.Record
import ca.sfu.iat.fintrack.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FirebaseHandler {
    private var database: DatabaseReference = Firebase.database.reference

    fun writeNewUser(user: User, uid: String) {
        database.child("users").child(uid).get().addOnSuccessListener {
            if (it.value == null) {
                database.child("users").child(uid).setValue(user)
            }
            Log.i("firebase", "Successfully added $it")
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun writeEntry(
        userId: String,
        budgetName: String,
        recordName: String,
        amount: Double,
        category: String,
        date: String,
        type: String
    ) {
        val key = database.child("users").child(userId).child("budgets").child(budgetName)
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
            "/users/$userId/budgets/$budgetName/records/$key" to postRecord
        )
        database.updateChildren(childUpdates)
    }
}
