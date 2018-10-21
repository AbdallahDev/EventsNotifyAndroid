package com.sarayrah.abdallah.eventsnotify.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        Log.d("fcm", p0?.notification?.body)
        Log.d("fcm", p0?.notification?.title)
    }
}