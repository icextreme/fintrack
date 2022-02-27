package ca.sfu.iat.fintrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ca.sfu.iat.fintrack.databinding.ActivityMainBinding
import ca.sfu.iat.fintrack.view.GraphFragment
import ca.sfu.iat.fintrack.view.PieFragment
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

        binding.button3.setOnClickListener {
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    replace<GraphFragment>(R.id.graphFragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }

        binding.button4.setOnClickListener {
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    replace<PieFragment>(R.id.graphFragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }

    }
}