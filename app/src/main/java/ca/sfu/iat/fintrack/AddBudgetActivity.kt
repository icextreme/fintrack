package ca.sfu.iat.fintrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.sfu.iat.fintrack.databinding.ActivityAddBudgetBinding
import ca.sfu.iat.fintrack.databinding.ActivityAddEntryBinding
import ca.sfu.iat.fintrack.databinding.ActivityLoginBinding

class AddBudgetActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddBudgetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_budget)

        binding = ActivityAddBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCreate.setOnClickListener {
            finish()
        }
    }
}