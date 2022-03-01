package ca.sfu.iat.fintrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.databinding.ActivityRegisterBinding
//import ca.sfu.iat.fintrack.databinding.ActivitySigninBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}