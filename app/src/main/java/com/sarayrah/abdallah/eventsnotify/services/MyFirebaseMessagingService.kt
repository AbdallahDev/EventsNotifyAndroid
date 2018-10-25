package com.sarayrah.abdallah.eventsnotify.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sarayrah.abdallah.eventsnotify.EventDetails
import com.sarayrah.abdallah.eventsnotify.R
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        Log.wtf("fcm", "" + p0?.data?.get("title"))
        Log.wtf("fcm", "" + p0?.data?.get("body"))

        // Create an explicit intent for an Activity in your app
        val notifyIntent = Intent(this, EventDetails::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("title", p0?.data?.get("title"))
            putExtra("body", p0?.data?.get("body"))
        }
        val notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        //make the notification builder
        val mBuilder = NotificationCompat.Builder(this,
                resources.getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(p0?.data?.get("title"))
                .setContentText(p0?.data?.get("body"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(p0?.data?.get("body")))
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
}