package com.example.streetcat.viewModel

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.streetcat.activity.PostActivity
import com.example.streetcat.adapter.CatInfoGalleryAdapter
import com.example.streetcat.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_post.*
import java.net.URL


class PostViewModel() : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    private var posts = ArrayList<GalleryPhoto>()

    private var postKey : String = ""
    private var commentKey : String = ""
    private var userKey : String = ""
    private var nickname : String = ""


    fun getPosts(): ArrayList<GalleryPhoto>{
        return posts
    }

    fun addPost(img: ArrayList<Uri?>, key: String) {
        posts.add(GalleryPhoto(img, key))
    }

    fun setPostRef(){ // 한 포스트에 대해서 push 함수로 키 만들어두고 그 키 값을 저장해둠
        postKey = database.getReference("posts").push().key.toString()
    }

    fun getKey() : String{
        return postKey
    }

    fun getPostRef(): DatabaseReference{
        return database.reference.child("posts")
    }

    fun getSchoolRef(): DatabaseReference{
        return database.reference.child("schools")
    }

    fun setSchool(key : String, school : String){
        database.getReference("posts").child(key).child("school").setValue(school)
    }

    private fun setImageUri(key: String, imageUri: String, index: Int){
        database.getReference("posts").child(key).child("pictures").child(index.toString()).setValue(imageUri)
    }

    fun setPost(key: String, post: PostClass){
        database.getReference("posts").child(key).setValue(post)
    }

    fun setPhoto(uris: ArrayList<Uri?>, key: String){
        for(i in 0 until uris.size){
            val uri=uris[i]
            if (uri != null) {
                val storafeRef = storage.reference.child(key + "_" + i.toString() + ".png") // 저장되는 파일 이름
                storafeRef?.putFile(uri).addOnSuccessListener {
                    storafeRef.downloadUrl.addOnSuccessListener { uri ->
                        setImageUri(key, uri.toString(), i)
                    }
                }
            }
        }
    }

    fun setCommentRef(key : String){ // 한 포스트에 대해서 push 함수로 키 만들어두고 그 키 값을 저장해둠
        commentKey = database.getReference("posts").child(key).child("comments").push().key.toString()
    }

    fun getCommentKey() : String{
        return commentKey
    }

    fun getCommentRef(key : String): DatabaseReference{
        return database.reference.child("posts").child(key).child("comments")
    }

    fun setComment(key : String, cKey : String, com : Comments){
        // val userImg : Uri, val username : String, val comment : String, val cnt : String
        database.getReference("posts").child(key).child("comments").child(cKey).child("userImg").setValue(com.userImg.toString())
        database.getReference("posts").child(key).child("comments").child(cKey).child("username").setValue(com.username)
        database.getReference("posts").child(key).child("comments").child(cKey).child("comment").setValue(com.comment)
        database.getReference("posts").child(key).child("comments").child(cKey).child("likeCnt").setValue(com.likeCnt)
    }

    fun getUserRef(): DatabaseReference{
        userKey = mAuth!!.currentUser.uid
        return database.getReference("users").child(userKey)
    }

    fun setNickname(nn : String){
        nickname = nn
    }
    fun getNickname() : String{
        return nickname
    }

    fun deletePost(key : String){
        database.getReference("posts").child(key).setValue(null)
    }

    fun deleteComment(postKey : String, commentKey : String){
        database.getReference("posts").child(postKey).child("comments").child(commentKey).setValue(null)
    }
}
