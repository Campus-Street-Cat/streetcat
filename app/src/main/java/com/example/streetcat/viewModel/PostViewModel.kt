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

    private var posts = ArrayList<GalleryPhoto>()
    private var _posts = MutableLiveData<ArrayList<GalleryPhoto>>()
    private var _postsRef = MutableLiveData<DatabaseReference>()

    private var comments = ArrayList<Comments>()

    private var postKey : String = ""


    fun getPosts(): ArrayList<GalleryPhoto>{
        return posts
    }

    fun addPost(img: ArrayList<Uri?>, key: String) {
        posts.add(GalleryPhoto(img, key))
        _posts.value = posts
        _postsRef.value = database.getReference("posts").child(key)
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

    fun setComment(key : String, cnt : Int, com : Comments){
        // val userImg : Uri, val username : String, val comment : String, val cnt : Int
        database.getReference("posts").child(key).child("comments").child(cnt.toString()).child("userImg").setValue(com.userImg.toString())
        database.getReference("posts").child(key).child("comments").child(cnt.toString()).child("username").setValue(com.username)
        database.getReference("posts").child(key).child("comments").child(cnt.toString()).child("comment").setValue(com.comment)
        database.getReference("posts").child(key).child("comments").child(cnt.toString()).child("cnt").setValue(com.cnt)
        database.getReference("posts").child(key).child("comments_cnt").setValue(cnt+1) // 댓글 수 하나 증가시켜줌

        Log.d("setComment", "끝")
    }
}
