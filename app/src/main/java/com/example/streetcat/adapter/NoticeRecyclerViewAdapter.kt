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
import com.example.streetcat.data.Notice
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_notices.view.*
import kotlinx.android.synthetic.main.item_recycler_view.view.*
import org.w3c.dom.Text

class NoticeRecyclerViewAdapter(private val noticeList: ArrayList<Notice>) :
    RecyclerView.Adapter<NoticeRecyclerViewAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onClick(view : View, position: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noticeType : TextView = view.notice_type
        val noticeContent : TextView = view.notice_content
        val imageView : ImageView = view.notice_image
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_notices, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.noticeContent.text = noticeList[position].context
        if(noticeList[position].type == "comment") {
            viewHolder.noticeType.text = "새로운 댓글이 달렸어요!"
            viewHolder.imageView.setImageResource(R.drawable.ic_cat_smile)
        }
        else{
            viewHolder.noticeType.text = "고양이 상태가 이상해요!"
            viewHolder.imageView.setImageResource(R.drawable.siren)
        }
        viewHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount() = noticeList.size
}