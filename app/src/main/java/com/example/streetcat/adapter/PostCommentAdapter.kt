package com.example.streetcat.adapter

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.item_comments.view.*
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.data.Comments
import com.example.streetcat.R
import com.example.streetcat.activity.MainActivity
import com.example.streetcat.activity.PostActivity
import com.example.streetcat.viewModel.PostViewModel
import com.squareup.picasso.Picasso

class PostCommentAdapter(private val commentList: ArrayList<Comments> , private val postViewModel: PostViewModel, private val key : String, private val cont : Context, private val menuInflater : MenuInflater) :
    RecyclerView.Adapter<PostCommentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView : ImageView
        val username: TextView
        val comment: TextView
        val cnt : TextView
        val heart_btn : ImageButton
        val more_btn : ImageButton

        init {
            imageView = view.comment_user_profile_image
            username = view.comment_user_name
            comment = view.comment
            cnt = view.comment_like_cnt
            heart_btn = view.comment_image_heart
            more_btn = view.comment_more_btn
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_comments, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(commentList[position].userImg).error(R.drawable.common_google_signin_btn_icon_dark).into(viewHolder.imageView)
        viewHolder.username.text = commentList[position].username
        viewHolder.comment.text = commentList[position].comment
        viewHolder.cnt.text = commentList[position].likeCnt

        postViewModel.getUserRef().child("nickName").get().addOnSuccessListener {
            postViewModel.setNickname(it.value.toString())
        }

        viewHolder.more_btn.setOnClickListener {
            val name = postViewModel.getNickname()
            val popup = PopupMenu(cont, it)
            menuInflater.inflate(R.menu.post_delete_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.deletePost -> {
                        println("this")
                        val deleteCheck = AlertDialog.Builder(cont)

                        deleteCheck.setTitle("댓글을 삭제하시겠습니까?")
                        deleteCheck.setPositiveButton("삭제"){ dialog, which ->
                            postViewModel.deleteComment(key, commentList[position].key)
                        }
                        deleteCheck.setNegativeButton("취소", null)
                        deleteCheck.show()
                    }
                }
                false
            }
            if(name == commentList[position].username) // 본인이 쓴 게시글만 삭제할 수 있도록 함
                popup.show()



//            postViewModel.getCommentRef(key).addValueEventListener(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//                override fun onDataChange(dataSnapshot: DataSnapshot){
//
//                }
//            })
        }
    }

    override fun getItemCount() = commentList.size
}