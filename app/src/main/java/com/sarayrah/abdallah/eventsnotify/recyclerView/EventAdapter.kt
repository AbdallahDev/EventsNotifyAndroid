package com.sarayrah.abdallah.eventsnotify.recyclerView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sarayrah.abdallah.eventsnotify.R
import kotlinx.android.synthetic.main.event_details_row.view.*

class EventAdapter(private val context: Context, private val eventsList: ArrayList<EventsDataSet>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        val v = LayoutInflater.from(context).inflate(R.layout.event_details_row, parent
                , false)

        return ViewHolder(v)
    }

    override fun getItemCount() = eventsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(eventsList[position].eventTitle)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(title: String) {
            itemView.textView_title.text = title
        }
    }
}
