package net.syllabus.whatsoup

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import java.util.Calendar

class NotifReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", 0)
//        val message = intent.getStringExtra("message")
//        val title = intent.getStringExtra("title")

        var week = ""
        context?.openFileInput("plats.txt").use { stream ->
            stream?.bufferedReader().use { week = it?.readText().toString() }
        }

        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_WEEK)
        val index = (day % 7) * 2

        val midi = week.split(MainActivity.Constants.SEP)[index]
        val soir = week.split(MainActivity.Constants.SEP)[index+1]

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, "my_channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 13) soir else midi)
            .setContentText("Glandu aller cuisine !")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // styling the notification

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}