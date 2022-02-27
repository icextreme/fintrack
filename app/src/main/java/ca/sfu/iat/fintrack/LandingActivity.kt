package ca.sfu.iat.fintrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ca.sfu.iat.fintrack.databinding.ActivityMainBinding

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.elevation = 0F
        supportActionBar?.setCustomView(R.layout.toolbar_title_layout)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}