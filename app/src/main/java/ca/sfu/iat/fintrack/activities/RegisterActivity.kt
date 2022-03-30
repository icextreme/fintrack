package ca.sfu.iat.fintrack.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.sfu.iat.fintrack.FirebaseHandler
import ca.sfu.iat.fintrack.databinding.ActivityRegisterBinding
import ca.sfu.iat.fintrack.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbHandler: FirebaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        dbHandler = FirebaseHandler()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            val email = binding.inputEmailEditText.text.toString()
            val password = binding.inputPasswordEditText.text.toString()
            val firstName = binding.inputFirstNameEditText.text.toString()
            val lastName = binding.inputLastNameEditText.text.toString()
            if (isInputValid(firstName, lastName, email, password)) {
                val user = User(firstName, lastName, email)
                registerUser(user, password)
            }
        }
    }

    private fun registerUser(user: User, password: String) {
        val tag = "RegisterActivity.registerUser"
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext, "Registration successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val currUser = auth.currentUser
                    if (currUser != null) {
                        dbHandler.writeNewUser(user, currUser.uid)
                    }
                    updateUI(currUser)
                } else {
                    Log.w(tag, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Registration failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun isInputValid(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Boolean {
        return !(firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank())
    }
}