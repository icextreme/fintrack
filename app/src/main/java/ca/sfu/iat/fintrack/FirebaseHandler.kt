package ca.sfu.iat.fintrack

import android.content.ContentValues
import android.util.Log
import ca.sfu.iat.fintrack.model.Record
import ca.sfu.iat.fintrack.model.Type
import ca.sfu.iat.fintrack.model.User
import ca.sfu.iat.fintrack.model.getScoreList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    fun writeEntry(userId: String, recordName: String, amount: Double, category: String, date: String, type: String) {
        val key = database.child("users").child(userId).child("records").push().key
        if (key == null) {
            Log.w(ContentValues.TAG, "Couldn't get push key for posts")
            return
        }
        val record = Record(recordName, amount, category, type)
        record.dateAdded = date
        val postRecord = record.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/users/$userId/records/$key" to postRecord
        )
        database.updateChildren(childUpdates)
    }
}
