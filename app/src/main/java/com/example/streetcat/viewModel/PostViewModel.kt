package com.example.streetcat.viewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var userImg : String = ""

    private var tempKey : String = ""

    private val searchList = ArrayList<GalleryPhoto>()
    lateinit var adapter: CatInfoGalleryAdapter


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
        database.getReference("posts").child(key).child("authorId").setValue(mAuth!!.currentUser.uid)
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

    fun setComment(key : String, cKey : String, com : Comments){
        database.getReference("posts").child(key).child("comments").child(cKey).child("userImg").setValue(com.userImg.toString())
        database.getReference("posts").child(key).child("comments").child(cKey).child("username").setValue(com.username)
        database.getReference("posts").child(key).child("comments").child(cKey).child("comment").setValue(com.comment)
    }

    fun setNotice(key: String, authorId: String, context: String, username: String){
        val noticeKey = database.getReference("users").child(authorId).child("notice").push().key.toString()
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("type").setValue("comment")
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("postkey").setValue(key)
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("context").setValue(context)
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("username").setValue(username)
    }

    fun getUserRef(): DatabaseReference{
        userKey = mAuth!!.currentUser.uid
        return database.getReference("users").child(userKey)
    }

    fun getUserKey(): String{
        return userKey
    }

    fun setUserImg(img : String){
        userImg = img
    }

    fun getUserImg() : String {
        return userImg
    }

    fun addUserImg(key : String, img : String) {
        database.getReference("posts").child(key).child("authorImg").setValue(img)
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

    fun setCatRef(){ // 한 포스트에 대해서 push 함수로 키 만들어두고 그 키 값을 저장해둠
        tempKey = database.getReference("temp").push().key.toString()
    }

    fun getCatRef(): DatabaseReference{
        return database.reference.child("temp")
    }

    fun getCatKey() : String {
        return tempKey
    }

    fun addCats(key : String, cats : ArrayList<Cat>){
        database.getReference("temp").child(key).setValue(null)
        for(cat in cats) {
            database.getReference("temp").child(key).child("cats").child(cat.name).setValue(cat.catid)
        }
    }

    fun deleteCats(key : String){
        database.getReference("temp").child(key).setValue(null)
    }

    fun addPostCats(postKey : String, cats : ArrayList<Cat>){
        for(cat in cats) {
            database.getReference("posts").child(postKey).child("cats").child(cat.name).setValue(cat.catid)
        }
    }

    fun addHeart(key : String, username : String){
        database.getReference("posts").child(key).child("users").child(username).setValue(username)
    }

    fun deleteHeart(key : String, username : String){
        database.getReference("posts").child(key).child("users").child(username).setValue(null)
    }

    fun addCommentHeart(postKey : String, commentKey : String, username : String){
        database.getReference("posts").child(postKey).child("comments").child(commentKey).child("users").child(username).setValue(username)
    }

    fun deleteCommentHeart(postKey : String, commentKey : String, username : String){
        database.getReference("posts").child(postKey).child("comments").child(commentKey).child("users").child(username).setValue(null)
    }

    fun showPostFragmentRcView(context: Context, post_gallery: RecyclerView, search_view : SearchView){
        database.reference.child("posts").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val uris = ArrayList<Uri?>()
                    var flag = true
                    for (comp in getPosts()) {
                        if (comp.key == data.key && comp.photo.isNotEmpty())
                            flag = false
                    }
                    if (flag) {
                        val cnt = data.child("cnt").value.toString().toInt()

                        for (idx in 0 until cnt) {
                            val v = data.child("pictures").child(idx.toString()).value
                            if (v != null)
                                uris.add(Uri.parse(v.toString()))
                        }

                        val key = data.key.toString()
                        if (uris.isNotEmpty())
                            addPost(uris, key)
                    }
                }

                post_gallery.layoutManager = GridLayoutManager(context, 3)
                adapter = CatInfoGalleryAdapter(getPosts())
                post_gallery.adapter = adapter

                search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText == "") {
                            adapter = CatInfoGalleryAdapter(getPosts())
                            post_gallery.adapter = adapter
                            searchList.clear()

                            adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    moveToPost(position, getPosts(), context)
                                }
                            })
                        }
                        return true
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        for (data in dataSnapshot.children) {
                            val cats = ArrayList<String>()
                            val uris = ArrayList<Uri?>()
                            val school = data.child("school").value.toString()
                            val temp = data.child("cats").children
                            for (cat in temp) {
                                cats.add(cat.key.toString())
                            }

                            val cnt = data.child("cnt").value.toString().toInt()

                            for (idx in 0 until cnt) {
                                val v = data.child("pictures").child(idx.toString()).value
                                if (v != null)
                                    uris.add(Uri.parse(v.toString()))
                            }

                            if (query != null && cats.contains(query) || school.contains(query.toString())) {
                                searchList.add(GalleryPhoto(uris, data.key.toString()))
                            }
                        }
                        adapter = CatInfoGalleryAdapter(searchList)
                        post_gallery.adapter = adapter

                        adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                            override fun onClick(view: View, position: Int) {
                                moveToPost(position, getPosts(), context)
                            }
                        })
                        return false
                    }
                })

                adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        moveToPost(position, getPosts(), context)
                    }
                })
            }
        })
    }

    fun moveToPost(position : Int, array : ArrayList<GalleryPhoto>, context : Context){
        val intent = Intent(context, PostActivity::class.java)
        intent.putExtra("postKey", Uri.parse(array[position].key).toString()) // 해당 게시글로 갈 수 있도록 key 값을 넘겨서 화면 전환
        intent.putExtra("username", getNickname())
        startActivity(context, intent, null)
    }
}
