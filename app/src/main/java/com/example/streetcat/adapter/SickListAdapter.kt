package com.example.streetcat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.R
import com.example.streetcat.data.SickList
import kotlinx.android.synthetic.main.item_sick_info.view.*


class SickListAdapter (private val sickList : ArrayList<SickList>) :
    RecyclerView.Adapter<SickListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView1: TextView = view.sickname
        val textView2: TextView = view.sickex
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_sick_info, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView1.text = sickList[position].sickname
        holder.textView2.text = sickList[position].sickex
    }

    override fun getItemCount() = sickList.size
}