package com.example.streetcat.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.streetcat.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_pager.view.*

class PostViewPagerAdapter(private val context: Context, private val items: ArrayList<Uri>) :
    RecyclerView.Adapter<PostViewPagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(items[position]).error(R.drawable.common_google_signin_btn_icon_dark).into(holder.imageUrl)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageUrl = view.imageSlider
    }
}