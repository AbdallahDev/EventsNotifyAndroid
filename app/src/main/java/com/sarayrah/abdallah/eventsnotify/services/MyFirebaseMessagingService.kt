package com.sarayrah.abdallah.eventsnotify.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sarayrah.abdallah.eventsnotify.EventDetails
import com.sarayrah.abdallah.eventsnotify.R
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        notificationBuilder(p0?.data?.get("title")!!, p0.data?.get("body")!!,
                p0.data?.get("date")!!, p0.data?.get("time")!!)
    }

    //this function to build the notification
    private fun notificationBuilder(title: String, body: String, date: String, time: String) {

        // Create an explicit intent for an Activity in your app
        val notifyIntent = Intent(this, EventDetails::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("title", title)
            putExtra("body", body)
            putExtra("date", date)
            putExtra("time", time)
        }
        val notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        //make the notification builder
        val mBuilder = NotificationCompat.Builder(this,
                resources.getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_stat_name_notification_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setDefaults(Notification.DEFAULT_ALL)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(notifyPendingIntent)
                .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            //create random notification id
            val notificationId = Random().nextInt()
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, mBuilder.build())
        }
    }

    //i'll not use this function for now coz i think the best practice in android is not awaking the
    //screen when notification is received, for example the whatsApp do the same
    @SuppressLint("InvalidWakeLockTag")
    private fun wakeUpScreen() {
        //wake up lock screen
        val pm = applicationContext.getSystemService(Context.POWER_SERVICE)
                as PowerManager
        val isScreenOn = pm.isScreenOn
        if (!isScreenOn) {
            val wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
                    "mywakelocktag")
            wl.acquire(10000)
            val wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "mycpulocktag")
            wl_cpu.acquire(10000)
        }
    }
}