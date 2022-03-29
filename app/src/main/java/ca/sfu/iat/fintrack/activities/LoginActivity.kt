package ca.sfu.iat.fintrack.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.sfu.iat.fintrack.MainActivity
import ca.sfu.iat.fintrack.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonLogin.setOnClickListener {
            if (isInputValid(
                    binding.inputEmailEditText.text.toString(),
                    binding.inputPasswordEditText.text.toString()
                )
            ) {
                signInUser(
                    binding.inputEmailEditText.text.toString(),
                    binding.inputPasswordEditText.text.toString()
                )
            }
        }

        binding.registerForFree.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun signInUser(email: String, password: String) {
        val tag = "LoginActivity.signInUser"
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "signInWithEmail:success")
                    Toast.makeText(
                        baseContext, "Login successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(tag, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Login failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun isInputValid(
        email: String,
        password: String
    ): Boolean {
        return !(email.isBlank() || password.isBlank())
    }
}