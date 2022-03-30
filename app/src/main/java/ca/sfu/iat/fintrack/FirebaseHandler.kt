package ca.sfu.iat.fintrack

import android.content.ContentValues
import android.util.Log
import ca.sfu.iat.fintrack.model.Record
import ca.sfu.iat.fintrack.model.Type
import ca.sfu.iat.fintrack.model.User
import ca.sfu.iat.fintrack.model.getScoreList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import java.time.LocalDate
import java.util.HashMap


class FirebaseHandler {
    private var database: DatabaseReference = Firebase.database.reference
    var recordsList = ArrayList<Record>()
    fun getRecords(userId: String) {
        val recordsQuery = database.child("users/$userId/records")
        recordsQuery.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val record: Record? = dataSnapshot.getValue<Record>()
                    if (record != null) {
                        recordsList.add(record)
                        println(record)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase", "loadPost:onCancelled", error.toException())
            }


        })
    }
    fun writeNewUser(userId: String, firstName: String, lastName: String, email: String) {
        val user = User(firstName, lastName, email)
        database.child("users").child(userId).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            if (it.value == null) {
                database.child("users").child(userId).setValue(user)
            }
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
        record.date = date
        val postRecord = record.toMap()

        val childUpdates = hashMapOf<String, Any>(
        "/users/$userId/records/$key" to postRecord
        )
        database.updateChildren(childUpdates)


    }


}
