package com.sarayrah.abdallah.eventsnotify.services

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sarayrah.abdallah.eventsnotify.EventDetails

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
    }
}