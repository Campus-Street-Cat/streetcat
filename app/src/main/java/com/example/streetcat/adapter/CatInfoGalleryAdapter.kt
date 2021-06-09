package com.example.streetcat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.R
import com.example.streetcat.data.GalleryPhoto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_gallery.view.*

// postFragment와 CatInfo에서 게시글을 보여주는 리사이클러 뷰에 대한 어댑터
class CatInfoGalleryAdapter(private val photos : ArrayList<GalleryPhoto>) :
        RecyclerView.Adapter<CatInfoGalleryAdapter.ViewHolder>() {

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