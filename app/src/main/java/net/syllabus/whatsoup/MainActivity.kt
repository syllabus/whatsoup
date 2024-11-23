package net.syllabus.whatsoup

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.syllabus.whatsoup.databinding.ActivityMainBinding
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    object Constants {
        const val SEP = "~"
        const val SAVE_INTENT = "net.syllabus.whatsoup.MENU_SAVED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        createNotificationChannel()
        scheduleRecurringNotification()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "un nom"
            val descriptionText = "une description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("my_channel_id", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleRecurringNotification(){
        val notificationIntent = Intent(this, NotifReceiver::class.java)
        notificationIntent.putExtra("notificationId", 2)
        notificationIntent.putExtra("title", "Titel")
        notificationIntent.putExtra("message", "maissaje")
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val interval = 1 * 24 * 60 * 60 * 1000 // interval in milliseconds
  //      val interval = 60000 // for every minute during testing
/*
        val test = Calendar.getInstance()
        test.add(Calendar.MINUTE, 1)

        alarmManager.
        setExact(
            AlarmManager.RTC_WAKEUP,
            test.timeInMillis,
            pendingIntent
        )
*/
        val cal1130 = Calendar.getInstance()
        cal1130.set(Calendar.HOUR_OF_DAY, 11)
        cal1130.set(Calendar.MINUTE, 30)
        cal1130.set(Calendar.SECOND, 0)
        cal1130.set(Calendar.MILLISECOND, 0)

        alarmManager.
        setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal1130.timeInMillis,
            interval.toLong(),
            pendingIntent
        )

        val cal1830 = Calendar.getInstance()
        cal1830.set(Calendar.HOUR_OF_DAY, 18)
        cal1830.set(Calendar.MINUTE, 30)
        cal1830.set(Calendar.SECOND, 0)
        cal1830.set(Calendar.MILLISECOND, 0)

        alarmManager.
        setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal1830.timeInMillis,
            interval.toLong(),
            pendingIntent
        )
    }
}