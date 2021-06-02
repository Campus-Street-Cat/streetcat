package com.example.streetcat.adapter

import android.content.Context
import android.graphics.Color
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
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.data.Comments
import com.example.streetcat.R
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import android.util.Log

class PostCommentAdapter(private val commentList: ArrayList<Comments> , private val postViewModel: PostViewModel, private val username : String,
                         private val key : String, private val cont : Context, private val menuInflater : MenuInflater) :
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

        postViewModel.getPostRef().addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = ArrayList<String>()
                for (data in dataSnapshot.children){
                    if(data.key == key){
                        val temp = data.child("comments").child(commentList[position].key).child("users").children // 좋아요 누른 유저 리스트
                        for(user in temp){
                            if(user != null){
                                users.add(user.key.toString())
                            }
                        }
                        viewHolder.cnt.text = users.size.toString()
                    }
                }

                if(users.contains(username)){
                    viewHolder.heart_btn.setColorFilter(Color.parseColor("#FF0000"))
                }

                viewHolder.heart_btn.setOnClickListener {
                    if(users.contains(username)){ // 이미 누른 상태에서 다시 누르는 거 -> 회색으로 바꾸고 DB에서 삭제
                        viewHolder.heart_btn.setColorFilter(Color.parseColor("#D0CFCF"))
                        users.remove(username)
                        postViewModel.deleteCommentHeart(key, commentList[position].key, username)
                    }
                    else{
                        viewHolder.heart_btn.setColorFilter(Color.parseColor("#FF0000"))
                        postViewModel.addCommentHeart(key, commentList[position].key, username)
                    }
                }
            }
        })


        viewHolder.more_btn.setOnClickListener {
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
            if(username == commentList[position].username) // 본인이 쓴 게시글만 삭제할 수 있도록 함
                popup.show()
        } // more_btn


    }

    override fun getItemCount() = commentList.size
}