package com.sarayrah.abdallah.eventsnotify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import com.sarayrah.abdallah.eventsnotify.data.Data
import com.sarayrah.abdallah.eventsnotify.recyclerView.EventsAdapter
import com.sarayrah.abdallah.eventsnotify.recyclerView.EventsDataSet
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val eventsList = ArrayList<EventsDataSet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)

        //use log fun
        logPrint()

        //here i check for the googlePlayService availability, coz the can't work without it.
        googlePlayServicesAvailable()

        //call the function that view the data
        eventsViewing()
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

    //recyclerView inflation code
    private fun recyclerViewInflation() {
        rv_events.hasFixedSize()
        rv_events.layoutManager = LinearLayoutManager(this)
        rv_events.adapter = EventsAdapter(eventsList)
    }

    //fill list for the recyclerView
    private fun eventsViewing() {
        //volley code
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, Data.getEventsUrl
                , null,
                Response.Listener { response ->
                    Log.wtf("fcm", "response: " + response.toString())
                    for (i in 0 until response.length()) {
                        eventsList.add(EventsDataSet(
                                response.getJSONObject(i).getString("committee_name"),
                                response.getJSONObject(i).getString("subject"),
                                response.getJSONObject(i).getString("event_date"),
                                response.getJSONObject(i).getString("time")))
                    }
                    recyclerViewInflation()
                }, Response.ErrorListener {
        })
        queue.add(jsonArrayRequest)
    }
}