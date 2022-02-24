package ca.sfu.iat.fintrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.sfu.iat.fintrack.databinding.ActivityMainBinding
import ca.sfu.iat.fintrack.view.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}