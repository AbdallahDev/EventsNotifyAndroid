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

        //here i check for the googlePlayService availability, coz the can't work without it.
        googlePlayServicesAvailable()
    }

    override fun onResume() {
        super.onResume()

        //here i check for the googlePlayService availability, coz the can't work without it.
        googlePlayServicesAvailable()
    }

    //this fun to check for the googlePlayService availability, coz the can't work without it.
    private fun googlePlayServicesAvailable() {
        GoogleApiAvailability.getInstance()
                .makeGooglePlayServicesAvailable(this)
    }

    //this fun to pint device instanceID in the LOG
    private fun logPrint() {
        Log.wtf("fcm", " ${FirebaseInstanceId.getInstance().token}")
    }
}