package com.sarayrah.abdallah.eventsnotify

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import android.widget.Toast
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GoogleApiAvailability.getInstance()
                .makeGooglePlayServicesAvailable(this)
                .addOnSuccessListener {
                    Log.d("fcm", "makeGooglePlayServicesAvailable().onSuccess()")

                    // GPS available; do something useful
                }.addOnFailureListener(this) { e ->
                    Log.d("fcm", "makeGooglePlayServicesAvailable().onFailure()")
                    e.printStackTrace()

                    // can't proceed without GPS; quit

                    this@MainActivity.finish() // this causes a crash
                }
    }
}