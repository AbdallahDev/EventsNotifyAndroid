package com.sarayrah.abdallah.eventsnotify.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        Log.wtf("fcm", "" + p0?.data?.get("title"))
        Log.wtf("fcm", "" + p0?.data?.get("body"))
    }
}