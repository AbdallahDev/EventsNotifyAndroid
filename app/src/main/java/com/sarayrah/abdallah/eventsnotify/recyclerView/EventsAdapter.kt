package com.sarayrah.abdallah.eventsnotify.recyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sarayrah.abdallah.eventsnotify.R
import kotlinx.android.synthetic.main.event_details_row.view.*

class EventsAdapter(private val list: ArrayList<EventsDataSet>) :
        RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.event_details_row, p0,
                false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.tv_title.text = list[p1].eventTitle
        p0.itemView.tv_title.text = list[p1].eventTitle
        p0.itemView.tv_title.text = list[p1].eventTitle
        p0.itemView.tv_title.text = list[p1].eventTitle
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}