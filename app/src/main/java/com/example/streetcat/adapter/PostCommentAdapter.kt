package com.example.streetcat.adapter

import kotlinx.android.synthetic.main.item_comments.view.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.data.Comments
import com.example.streetcat.R

class PostCommentAdapter(private val commentList: ArrayList<Comments>) :
    RecyclerView.Adapter<PostCommentAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onClick(view : View, position: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView : ImageView
        val username: TextView
        val comment: TextView

        init {
            imageView = view.comment_user_profile_image
            username = view.comment_user_name
            comment = view.comment
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_comments, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.imageView.setImageResource(commentList[position].user_img)
        viewHolder.username.text = commentList[position].user_name
        viewHolder.comment.text = commentList[position].comment

        viewHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount() = commentList.size
}