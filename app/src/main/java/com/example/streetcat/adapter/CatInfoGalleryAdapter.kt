package com.example.streetcat.adapter

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.streetcat.R
import com.example.streetcat.data.GalleryPhoto
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_gallery.view.*


class CatInfoGalleryAdapter(private val photos : ArrayList<GalleryPhoto>) :
        RecyclerView.Adapter<CatInfoGalleryAdapter.ViewHolder>() { //, Filterable {

    private var searchList = ArrayList<GalleryPhoto>()

    interface ItemClickListener{
        fun onClick(view : View, position: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView : ImageView = view.gallery_image
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_gallery, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(photos[position].photo.isNotEmpty())
            Picasso.get().load(photos[position].photo[0]).error(R.drawable.common_google_signin_btn_icon_dark).into(viewHolder.imageView)
        else
            Picasso.get().load(R.drawable.common_google_signin_btn_icon_dark).error(R.drawable.common_google_signin_btn_icon_dark).into(viewHolder.imageView)

        viewHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount() = photos.size
}