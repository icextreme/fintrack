package ca.sfu.iat.fintrack.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.sfu.iat.fintrack.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            // TODO: Add user first and last names to database
            if (isInputValid(
                    binding.inputFirstNameEditText.text.toString(),
                    binding.inputLastNameEditText.text.toString(),
                    binding.inputEmailEditText.text.toString(),
                    binding.inputPasswordEditText.text.toString()
                )
            ) {
                registerUser(
                    binding.inputEmailEditText.text.toString(),
                    binding.inputPasswordEditText.text.toString()
                )
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        val tag = "RegisterActivity.registerUser"
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext, "Registration successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    updateUI(user)
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