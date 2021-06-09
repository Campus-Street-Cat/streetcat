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

    private var postKey : String = ""
    private var commentKey : String = ""
    private var userKey : String = ""
    private var nickname : String = ""
    private var userImg : String = ""
    private var tempKey : String = ""

    private val searchList = ArrayList<GalleryPhoto>()
    private var posts = ArrayList<GalleryPhoto>()
    lateinit var adapter: CatInfoGalleryAdapter

    fun getPosts(): ArrayList<GalleryPhoto>{ // posts 반환 함수
        return posts
    }

    fun addPost(img: ArrayList<Uri?>, key: String) { // posts에 새 post 추가
        posts.add(GalleryPhoto(img, key))
    }

    fun setPostRef(){ // 한 포스트에 대해서 push 함수로 키 만들어두고 그 키 값을 저장해둠
        postKey = database.getReference("posts").push().key.toString()
    }

    fun getKey() : String{ // 새 게시글의 postKey 반환
        return postKey
    }

    fun getPostRef(): DatabaseReference{ // 파이어베이스에서 "posts" 레퍼런스 반환
        return database.reference.child("posts")
    }

    fun getSchoolRef(): DatabaseReference{ // 파이어베이스에서 "schools" 레퍼런스 반환
        return database.reference.child("schools")
    }

    fun setSchool(key : String, school : String){ // DB에 게시글의 학교정보 저장
        database.getReference("posts").child(key).child("school").setValue(school)
    }

    private fun setImageUri(key: String, imageUri: String, index: Int){ // DB에 게시글의 사진 저장
        database.getReference("posts").child(key).child("pictures").child(index.toString()).setValue(imageUri)
    }

    fun setPost(key: String, post: PostClass){ // 새로 작성한 게시글
        database.getReference("posts").child(key).setValue(post)
        database.getReference("posts").child(key).child("authorId").setValue(mAuth!!.currentUser.uid)
    }

    fun setPhoto(uris: ArrayList<Uri?>, key: String){ // uri로 받아온 사진을 파이어베이스에 저장할 수 있는 url로 변경해서 저장
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

    fun setCommentRef(key : String){ // 한 comment에 대해서 push 함수로 키 만들어두고 그 키 값을 저장해둠
        commentKey = database.getReference("posts").child(key).child("comments").push().key.toString()
    }

    fun getCommentKey() : String{ // comment 키 받아옴
        return commentKey
    }

    fun setComment(key : String, cKey : String, com : Comments){ // 한 post에 대해서 새 댓글을 파이어베이스에 저장
        database.getReference("posts").child(key).child("comments").child(cKey).child("userImg").setValue(com.userImg.toString())
        database.getReference("posts").child(key).child("comments").child(cKey).child("username").setValue(com.username)
        database.getReference("posts").child(key).child("comments").child(cKey).child("comment").setValue(com.comment)
    }

    fun setNotice(key: String, authorId: String, context: String, username: String){ // 댓글이 달리면 알림 띄움
        val noticeKey = database.getReference("users").child(authorId).child("notice").push().key.toString()
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("type").setValue("comment")
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("postkey").setValue(key)
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("context").setValue(context)
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("username").setValue(username)
    }

    fun getUserRef(): DatabaseReference{ // 파이어베이스에서 현재 유저의 레퍼런스 반환
        userKey = mAuth!!.currentUser.uid
        return database.getReference("users").child(userKey)
    }

    fun getUserKey(): String{ // 유저 키값 반환
        return userKey
    }

    fun setUserImg(img : String){ // 유저의 프로필 이미지 set
        userImg = img
    }

    fun getUserImg() : String { // 유저의 프로필 이미지 반환
        return userImg
    }

    fun addUserImg(key : String, img : String) { // 게시글을 작성할 때 작성한 유저의 프로필 이미지 저장
        database.getReference("posts").child(key).child("authorImg").setValue(img)
    }

    fun setNickname(nn : String){ // 유저의 닉네임 set
        nickname = nn
    }

    fun getNickname() : String{ // 유저의 닉네임 반환
        return nickname
    }

    fun deletePost(key : String){ // DB에서 게시글 삭제
        database.getReference("posts").child(key).setValue(null)
    }

    fun deleteComment(postKey : String, commentKey : String){ // DB에서 댓글 삭제
        database.getReference("posts").child(postKey).child("comments").child(commentKey).setValue(null)
    }

    fun setCatRef(){ // writePost에서 선택한 고양이를 임시로 저장하기 위한 key set
        tempKey = database.getReference("temp").push().key.toString()
    }

    fun getCatRef(): DatabaseReference{ // temp에 대한 레퍼런스 반환
        return database.reference.child("temp")
    }

    fun getCatKey() : String { // temp 키값 반환
        return tempKey
    }

    fun addCats(key : String, cats : ArrayList<Cat>){ // writePost에서 선택한 고양이를 DB에 저장
        database.getReference("temp").child(key).setValue(null)
        for(cat in cats) {
            database.getReference("temp").child(key).child("cats").child(cat.name).setValue(cat.catid)
        }
    }

    fun deleteCats(key : String){ // 게시글이 작성되면 임시로 저장했던 고양이(temp)에 대한 DB 삭제
        database.getReference("temp").child(key).setValue(null)
    }

    fun addPostCats(postKey : String, cats : ArrayList<Cat>){ // 게시글에 선택된 고양이 정보 저장
        for(cat in cats) {
            database.getReference("posts").child(postKey).child("cats").child(cat.name).setValue(cat.catid)
        }
    }

    fun addHeart(key : String, username : String){ // 한 게시글에 대해 좋아요를 누른 유저 이름 저장
        database.getReference("posts").child(key).child("users").child(username).setValue(username)
    }

    fun deleteHeart(key : String, username : String){ // 게시글 좋아요 취소했을 때 DB에서 정보 삭제
        database.getReference("posts").child(key).child("users").child(username).setValue(null)
    }

    fun addCommentHeart(postKey : String, commentKey : String, username : String){ // 한 댓글에 대해 좋아요를 누른 유저 이름 저장
        database.getReference("posts").child(postKey).child("comments").child(commentKey).child("users").child(username).setValue(username)
    }

    fun deleteCommentHeart(postKey : String, commentKey : String, username : String){ // 댓글 좋아요 취소했을 때 DB에서 정보 삭제
        database.getReference("posts").child(postKey).child("comments").child(commentKey).child("users").child(username).setValue(null)
    }

    fun showPostFragmentRcView(context: Context, post_gallery: RecyclerView, search_view : SearchView){ // postFragment에서 리사이클러뷰에 실시간으로 DB 데이터 가져와서 보여주기
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

                // 검색창에 단어 입력하면 필터링 하는 코드
                search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean { // 검색 단어가 없을 때 모든 게시글 보여주게 바꿈
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
                        for (data in dataSnapshot.children) { // 검색 단어를 submit 했을 때 해당되는 게시물만 필터링해서 리사이클러 뷰에 띄움
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

                // 리사이클러뷰에서 각 item의 클릭 리스너
                adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        moveToPost(position, getPosts(), context)
                    }
                })
            }
        })
    }

    fun moveToPost(position : Int, array : ArrayList<GalleryPhoto>, context : Context){ // 선택된 게시글로 이동하는 함수
        val intent = Intent(context, PostActivity::class.java)
        intent.putExtra("postKey", Uri.parse(array[position].key).toString())
        intent.putExtra("username", getNickname())
        startActivity(context, intent, null)
    }
}
