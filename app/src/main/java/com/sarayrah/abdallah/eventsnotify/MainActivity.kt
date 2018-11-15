package com.sarayrah.abdallah.eventsnotify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.d
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import com.sarayrah.abdallah.eventsnotify.data.Data
import com.sarayrah.abdallah.eventsnotify.recyclerView.EventsAdapter
import com.sarayrah.abdallah.eventsnotify.recyclerView.EventsDataSet
import com.sarayrah.abdallah.eventsnotify.spinner.CommitteesDataSet
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val eventsList = ArrayList<EventsDataSet>()
    private val committeesList = ArrayList<CommitteesDataSet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)

        //use log fun
        logPrint()

        //here i check for the googlePlayService availability, coz the can't work without it.
        googlePlayServicesAvailable()

        //create notification channel
        createNotificationChannel()

        //call the method to fill the spinner.
        spinnerFil()
    }

    override fun onResume() {
        super.onResume()

        //use log fun
        logPrint()

        //here i check for the googlePlayService availability, coz the can't work without it.
        googlePlayServicesAvailable()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val committee = p0?.selectedItem as CommitteesDataSet
        eventsViewing(committee)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    //this fun to print device instanceID in the LOG
    private fun logPrint() {
        d("fcm", " ${FirebaseInstanceId.getInstance().token}")
    }

    //recyclerView inflation code
    private fun recyclerViewInflation() {
        rv_events.hasFixedSize()
        rv_events.layoutManager = LinearLayoutManager(this)
        rv_events.adapter = EventsAdapter(eventsList)
    }

    //fill list for the recyclerView, and i've defined the default object value coz i want to get
    //all the events when the committee not chosen, like on activity onCreate or nothing been chosen
    //from the spinner
    private fun eventsViewing(committee: CommitteesDataSet =
                                      CommitteesDataSet(0, "جميع اللجان")) {

        //progress dialog code
        val pd = ProgressDialog(this)
        pd.setMessage("يرجى الانتظار...")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        //volley code
        val url = Data.getEventsUrl + committee.id
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url
                , null,
                Response.Listener { response ->
                    //i'll hide the loading dialog
                    pd.hide()
                    d("fcm", "responseEvents: " + response.toString())
                    eventsList.clear()
                    if (response.length() != 0) {
                        for (i in 0 until response.length()) {
                            //this variable to store the value of the entity name from the database
                            //coz it could contain the committee_name value or the event_entity_name value
                            var eventEntity = ""
                            //here i'll check if the value of the entity name is the committee name
                            //from the dropdown menu
                            //or the event_entity_name that typed in the website app text box
                            if (response.getJSONObject(i).getString("event_entity_name").isNullOrEmpty()) {
                                //here this means the value of the entity_name is from the committee_name
                                //coz the value of the event_entity_name is empty.
                                eventEntity = response.getJSONObject(i).getString("committee_name")
                            } else {
                                //here this means the value of the entity_name is from the event_entity_name
                                //coz the value of the event_entity_name is not empty, so that means
                                //the value of the committee_name is exist.
                                eventEntity = response.getJSONObject(i).getString("event_entity_name")
                            }
                            eventsList.add(EventsDataSet(
                                    eventEntity,
                                    response.getJSONObject(i).getString("subject"),
                                    response.getJSONObject(i).getString("event_date"),
                                    response.getJSONObject(i).getString("time")))
                        }
                    } else {
                        Toast.makeText(this, "لا يوجد نشاطات", Toast.LENGTH_LONG).show()
                    }
                    recyclerViewInflation()
                }, Response.ErrorListener { error ->
            //i'll hide the loading dialog
            pd.hide()
            d("fcm", "responseEventsError: ${error.message}")
        })
        queue.add(jsonArrayRequest)
    }

    //fill spinner method
    private fun spinnerFil() {
        //this is the first element in the spinner
        committeesList.add(CommitteesDataSet(0, "جميع اللجان"))

        //progress dialog code
        val pd = ProgressDialog(this)
        pd.setMessage("يرجى الانتظار...")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        //volley code
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, Data.getCommitteesUrl
                , null,
                Response.Listener { response ->
                    pd.hide()
                    for (i in 0 until response.length()) {
                        committeesList.add(CommitteesDataSet(
                                response.getJSONObject(i).getInt("committee_id"),
                                response.getJSONObject(i).getString("committee_name")))
                    }
                    val spinnerAdapter = ArrayAdapter(this,
                            android.R.layout.simple_spinner_item, committeesList)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner_committees.adapter = spinnerAdapter
                    spinner_committees.onItemSelectedListener = this
                }, Response.ErrorListener { error ->
            pd.hide()
            d("fcm", "responseCommitteesError: ${error.message}")
        })
        queue.add(jsonArrayRequest)
    }
}