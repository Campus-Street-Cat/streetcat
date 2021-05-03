package com.example.streetcat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.example.streetcat.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycler_view.view.*

class SickListAdapter (private val sickList : ArrayList<SearchableItem>) :
    RecyclerView.Adapter<SickListAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.name
        }
        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SickListAdapter.ViewHolder =
            SickListAdapter.ViewHolder(
            // Create a new view, which defines the UI of the list item
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_sick_info, viewGroup, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = sickList[position].text
    }

    override fun getItemCount() = sickList.size
}