package ca.sfu.iat.fintrack.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
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

        val periodSpinner = binding.spinnerPeriod

        ArrayAdapter.createFromResource(
            this,
            R.array.period_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            periodSpinner.adapter = adapter
        }
    }
}