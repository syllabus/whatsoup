package net.syllabus.whatsoup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.syllabus.whatsoup.databinding.ActivityMain2Binding


class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_week, R.id.navigation_template
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        onSharedIntent()
    }

    private fun onSharedIntent() {
        val receivedIntent = intent
        val receivedAction = receivedIntent.action
        val receivedType = receivedIntent.type

        if (receivedAction == Intent.ACTION_SEND) {
            Log.e("WHATSOUP", "onSharedIntent: " + receivedType + " shared")
            if (receivedType!!.startsWith("text/")) {
                val receivedText = receivedIntent
                    .getStringExtra(Intent.EXTRA_TEXT)
                if (receivedText != null) {
                    Log.e("WHATSOUP", receivedText)
                }
            }
        } else if (receivedAction == Intent.ACTION_MAIN) {
            Log.e("WHATSOUP", "onSharedIntent: nothing shared")
        }
    }
}