package com.sarayrah.abdallah.eventsnotify

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import kotlinx.android.synthetic.main.activity_event_details.*

class EventDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        tv_title.text = Html.fromHtml(intent.getStringExtra("title"))
        tv_body.text = Html.fromHtml(intent.getStringExtra("body"))
        tv_date.text = Html.fromHtml(intent.getStringExtra("date"))
        tv_time.text = Html.fromHtml(intent.getStringExtra("time"))

        //by this button i'll navigate to the activity that view all the events.
        btn_all_events.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }
}
