package com.example.streetcat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.streetcat.R
import kotlinx.android.synthetic.main.item_view_pager.view.*

/*class ViewPagerAdapter(var postImage: ArrayList<Int>) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = postImage.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.img.setImageResource(postImage[position])
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)){

        val img = itemView.imageSlider
    }
}*/

class PostViewPagerAdapter(private val context: Context, private val items: ArrayList<Int>) :
    RecyclerView.Adapter<PostViewPagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(items[position]).into(holder.imageUrl)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageUrl = view.imageSlider
    }
}