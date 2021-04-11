package com.example.streetcat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_gallery.view.*

class GalleryAdapter(private val photos : ArrayList<GalleryPhoto>) :
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onClick(view : View, position: Int)
    }

    private lateinit var itemClickListener :  ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView : ImageView

        init {
            imageView = view.gallery_image
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_gallery, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.imageView.setImageResource(photos[position].photo)

        viewHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount() = photos.size
}