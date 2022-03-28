package ca.sfu.iat.fintrack

import android.util.Log
import ca.sfu.iat.fintrack.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import java.util.HashMap


class FirebaseHandler {
    private var database: DatabaseReference = Firebase.database.reference

    fun writeNewUser(userId: String, firstName: String, lastName: String, email: String) {
        val user = User(firstName, lastName, email)
        database.child("users").child(userId).setValue(user)
    }
}
