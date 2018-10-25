package com.sarayrah.abdallah.eventsnotify

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_event_details.*

class EventDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        tv_title.text = intent.getStringExtra("title")
        tv_title.text = intent.getStringExtra("body")
        tv_title.text = intent.getStringExtra("date")
        tv_title.text = intent.getStringExtra("time")
    }
}
