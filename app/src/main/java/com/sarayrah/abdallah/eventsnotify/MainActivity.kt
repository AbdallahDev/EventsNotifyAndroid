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
import com.sarayrah.abdallah.eventsnotify.spinner.CategoriesDataSet
import com.sarayrah.abdallah.eventsnotify.spinner.CommitteesDataSet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    //this var to hold the events data
    private val eventsList = ArrayList<EventsDataSet>()
    //this var to hold the entities data
    private val committeesList = ArrayList<CommitteesDataSet>()
    //this var to hold the entity categories
    private val categoriesList = ArrayList<CategoriesDataSet>()
    //this var used to get the events for the specified category
    private var categoryId: Int = 0
    //this var used to get the events for the specified entity
    private var entityId: Int = 0

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

        //call the function that fill the categories spinner
        categoriesSpinnerFill()
    }

    override fun onResume() {
        super.onResume()

        //use log fun
        logPrint()

        //here i check for the googlePlayService availability, coz the can't work without it.
        googlePlayServicesAvailable()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //here i'll check which spinner choosed depended on the id
        when (p0?.id) {
            //this is the categories spinner id
            2131165316 -> {
                when (p2) {
                    //here this choice represent the default first choice
                    0 -> {
                        //her i'll hide the entities spinner
                        spinner_committees.visibility = View.GONE
                        //then here i'll view all the events coz no specific entity chosen
                        eventsViewing()
                    }
                    //here this choice represent the all other choices
                    else -> {
                        //i declared this variable to get the selected item as the CategoriesDataSet
                        //so i can get the id for the selected category form the db.
                        val category = p0.selectedItem as CategoriesDataSet
                        //here i get the category id for the selected category then store it in
                        // the global categoryId var to get the right events for that category
                        categoryId = category.id
                        //here i fill the entities spinner based on the chosen
                        entitiesSpinnerFill(categoryId)
                        spinner_committees.visibility = View.VISIBLE
                        eventsViewing(categoryId, entityId)
                    }
                }
            }
            //this is the entities spinner id
            2131165317 -> {
                //this val to store the chosen entity
                val entity = p0.selectedItem as CommitteesDataSet
                //this val to store the entity id for the selected entity to view it's related events
                entityId = entity.id
                eventsViewing(categoryId, entityId)
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //fill the categories spinner
    private fun categoriesSpinnerFill() {
        //this is the first element in the spinner, it's needed as the default value
        categoriesList.add(CategoriesDataSet(0, "جميع الفئات"))

        //progress dialog code
        val pd = ProgressDialog(this)
        pd.setMessage("يرجى الانتظار...")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        //volley code to get all the categories from the api
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, Data.getCategoriesUrl
                , null,
                Response.Listener { response ->
                    pd.hide()
                    for (i in 0 until response.length()) {
                        categoriesList.add(CategoriesDataSet(
                                response.getJSONObject(i).getInt("event_entity_category_id"),
                                response.getJSONObject(i).getString("event_entity_category_name")))
                    }
                    val spinnerAdapter = ArrayAdapter(this,
                            android.R.layout.simple_spinner_item, categoriesList)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner_categories.adapter = spinnerAdapter
                    spinner_categories.onItemSelectedListener = this
                }, Response.ErrorListener { error ->
            pd.hide()
            d("fcm", "responseCategoriesError: ${error.message}")
        })
        queue.add(jsonArrayRequest)
    }

    //this method fill the entities spinner based on the chosen entities category
    private fun entitiesSpinnerFill(categoryId: Int) {
        //here i'll clear the list so it dosen't stack the entities in the spinner every time it filled
        committeesList.clear()
        //this is the first element in the spinner, i use it as the default choice
        committeesList.add(CommitteesDataSet(0, "جميع الجهات"))

        //progress dialog code
        val pd = ProgressDialog(this)
        pd.setMessage("يرجى الانتظار...")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        //volley code
        val url = Data.getCommitteesUrl + categoryId
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url
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

    //this fun fill the list for the recyclerView based on the selected category and entity,
    // and when no category and entity chosen all the events will be fetched.
    private fun eventsViewing(categoryId: Int = 0, entityId: Int = 0) {

        //progress dialog code
        val pd = ProgressDialog(this)
        pd.setMessage("يرجى الانتظار...")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        //volley code
        //bellow i'll bind the category id and the entity id with the url to get the events for
        // the specified category and entity
        val url = Data.getEventsUrl + "categoryId=" + categoryId + "&" + "entityId=" + entityId
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url
                , null,
                Response.Listener { response ->
                    //i'll hide the loading dialog
                    pd.hide()
                    eventsList.clear()
                    if (response.length() != 0) {
                        for (i in 0 until response.length()) {
                            //this variable to store the value of the entity name from the database
                            //coz it could contain the committee_name value or the event_entity_name value
                            val eventEntity: String =
                                    if (!response.getJSONObject(i).getString("event_entity_name").isNullOrEmpty()) {
                                        //here this means the value of the entity_name is from the committee_name
                                        //coz the value of the event_entity_name is empty.
                                        response.getJSONObject(i).getString("event_entity_name")
                                    } else {
                                        response.getJSONObject(i).getString("entity_name")
                                    }
                            //here i'll check if the value of the entity name is sat from
                            // the event entity name record, to make it the event title
                            eventsList.add(EventsDataSet(
                                    eventEntity,
                                    response.getJSONObject(i).getString("subject"),
                                    response.getJSONObject(i).getString("event_date"),
                                    response.getJSONObject(i).getString("time")))
                        }
                    } else {
                        Toast.makeText(this, "لا يوجد نشاطات", Toast.LENGTH_SHORT).show()
                    }
                    recyclerViewInflation()
                }, Response.ErrorListener { error ->
            //i'll hide the loading dialog
            pd.hide()
            d("fcm", "responseEventsError: ${error.message}")
        })
        queue.add(jsonArrayRequest)
    }

    //recyclerView inflation code
    private fun recyclerViewInflation() {
        rv_events.hasFixedSize()
        rv_events.layoutManager = LinearLayoutManager(this)
        rv_events.adapter = EventsAdapter(eventsList)
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
}