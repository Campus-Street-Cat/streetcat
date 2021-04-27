package com.example.streetcat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.streetcat.data.Cat
import com.example.streetcat.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycler_view.view.*


class HomeRecyclerViewAdapter(private val catList: ArrayList<Cat>) :
    RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onClick(view : View, position: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.name
        val imageView : ImageView = view.image
        val view2: View = view

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_recycler_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = catList[position].name
        //Picasso.get().load(catList[position].img).error(R.drawable.common_google_signin_btn_icon_dark).into(viewHolder.imageView)
        Glide.with(viewHolder.imageView).load(catList[position].img).into(viewHolder.imageView)
        viewHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount() = catList.size
}