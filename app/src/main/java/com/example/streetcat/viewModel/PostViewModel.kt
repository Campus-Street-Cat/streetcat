package com.example.streetcat.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.streetcat.data.Cat
import com.example.streetcat.data.GalleryPhoto
import com.example.streetcat.data.Post
import com.example.streetcat.data.PostClass
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.net.URL


class PostViewModel() : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var posts = ArrayList<GalleryPhoto>()
    private var _posts = MutableLiveData<ArrayList<GalleryPhoto>>()
    private var _postsRef = MutableLiveData<DatabaseReference>()

    private var postKey : String = ""

    fun getPosts(): ArrayList<GalleryPhoto>{
        return posts
    }

    fun addPost(img: Uri, key: String){
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

    fun getLiveRef(): MutableLiveData<DatabaseReference>{
        return _postsRef
    }
    private fun setImageUri(key: String, imageUri: String){
        database.getReference("posts").child(key).child("picture").setValue(imageUri)
    }

    fun setPost(key: String, post: PostClass){
        database.getReference("posts").child(key).setValue(post)
    }

    fun setPhoto(uri: Uri, key: String){
        val storafeRef = storage.reference.child(key).child(key + ".png")
        storafeRef?.putFile(uri).addOnSuccessListener {
            storafeRef.downloadUrl.addOnSuccessListener { uri ->
                setImageUri(key, uri.toString())
            }

//            val imageUri = it.uploadSessionUri.toString()
//            val uri : String = storage.reference.child(key).downloadUrl
//            setImageUri(key, imageUri)
//            //setImageUri(key, uri.toString())
        }
    }

    fun getPhoto(key: String) : Uri{
        var tmpUri : Uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/streetcat-fd0b0.appspot.com/o/cats%2FKakaoTalk_20210310_215259084_15.jpg?alt=media&token=4a4a8012-3d95-4c2f-8aeb-32a826d6599f")
        Log.d("error", key)
        val ref = storage.reference.child("caticon.PNG")
        ref.downloadUrl.addOnSuccessListener {
            tmpUri = it
            Log.d("error", "error1")
        }.addOnFailureListener{
            Log.d("error", "error2")
        }
        return tmpUri
    }
}
