package com.sarayrah.abdallah.eventsnotify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Adapter
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import com.sarayrah.abdallah.eventsnotify.recyclerView.EventsDataSet
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val eventsList = ArrayList<EventsDataSet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //use log fun
        logPrint()

        //here i check for the googlePlayService availability, coz the can't work without it.
        googlePlayServicesAvailable()

        //recyclerView code
        recyclerView_eventDetails.apply {
            setHasFixedSize(true)
            LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()

        //use log fun
        logPrint()

        //here i check for the googlePlayService availability, coz the can't work without it.
        googlePlayServicesAvailable()
    }

    //this fun to check for the googlePlayService availability, coz the can't work without it.
    private fun googlePlayServicesAvailable() {
        GoogleApiAvailability.getInstance()
                .makeGooglePlayServicesAvailable(this)
    }

    //this fun to create notification channel on android oreo and higher.
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                    resources.getString(R.string.default_notification_channel_id), name, importance)
                    .apply {
                        description = descriptionText
                    }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    //this fun to pint device instanceID in the LOG
    private fun logPrint() {
        Log.wtf("fcm", " ${FirebaseInstanceId.getInstance().token}")
    }

    //function to fill eventsList Array with data for the adapter
    fun fillEventsList() {
        eventsList.add(EventsDataSet("title1"))
        eventsList.add(EventsDataSet("title2"))
        eventsList.add(EventsDataSet("title3"))
        eventsList.add(EventsDataSet("title4"))
        eventsList.add(EventsDataSet("title5"))
    }
}