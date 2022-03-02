package ca.sfu.iat.fintrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.sfu.iat.fintrack.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.register.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}