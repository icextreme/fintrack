package ca.sfu.iat.fintrack.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.databinding.ActivityAddBudgetBinding

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