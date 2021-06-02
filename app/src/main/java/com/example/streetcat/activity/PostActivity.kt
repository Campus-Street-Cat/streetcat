package com.example.streetcat.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.streetcat.data.Comments
import com.example.streetcat.R
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.adapter.PostCatNameAdapter
import com.example.streetcat.adapter.PostCommentAdapter
import com.example.streetcat.adapter.PostViewPagerAdapter
import com.example.streetcat.data.Cat
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    lateinit var adapter: PostViewPagerAdapter
    lateinit var commentAdapter : PostCommentAdapter
    lateinit var postCatNameAdapter : PostCatNameAdapter

    private val postViewModel: PostViewModel by viewModels()
    lateinit var postAuthor : String
    lateinit var key : String
    lateinit var username : String

    var imm : InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        val postKey : String? = intent.getStringExtra("postKey")
        val userName : String? = intent.getStringExtra("username")
        val cont = this
        if(postKey != null)
            key = postKey
        if(userName != null)
            username = userName

        postViewModel.getUserRef().child("picture").get().addOnSuccessListener{
            postViewModel.setUserImg(it.value.toString())
        }

        postViewModel.getPostRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val uri = ArrayList<Uri>()
                val cmts = ArrayList<Comments>()
                val catList = ArrayList<Cat>()
                val users = ArrayList<String>()

                for (data in dataSnapshot.children) {
                    if(data.key == postKey){ // 키 값이 같은 데이터를 찾으면
                        val cnt = data.child("cnt").value.toString() // 사진 개수
                        tv_total.text = cnt


                        Picasso.get().load(data.child("authorImg").value.toString()).error(R.drawable.common_google_signin_btn_icon_dark).into(user_profile_image)

                        postAuthor = data.child("author").value.toString()
                        user_name.text = data.child("author").value.toString() // 작성자 이름 뿌리기
                        context.text = data.child("contents").value.toString() // 글 본문 내용 뿌리기
                        post_school.text = "#" + data.child("school").value.toString() // 학교 뿌리기


                        for(i in 0 until cnt.toInt()){
                            uri.add(Uri.parse(data.child("pictures").child(i.toString()).value.toString())) // 사진 uri
                        }

                        // 해당 게시물의 댓글 가져오기
                        var temp = data.child("comments").children
                        var i = 0

                        for(cmt in temp){ // 댓글 내용
                            val profile = Uri.parse(cmt.child("userImg").value.toString())
                            val name = cmt.child("username").value.toString()
                            val c = cmt.child("comment").value.toString()
                            val commentKey = cmt.key.toString()
                            cmts.add(Comments(profile, name, c, commentKey))
                            i++
                        }
                        com_count.text = i.toString() // 댓글 개수

                        temp = data.child("cats").children // 게시글에 등록된 고양이 리스트 완성
                        for(cat in temp){
                            catList.add(Cat(Uri.parse(""),cat.key.toString(), cat.value.toString()))
                        }

                        temp = data.child("users").children // 좋아요 누른 유저 리스트
                        for(user in temp){
                            if(user != null){
                                users.add(user.key.toString())
                            }
                        }
                        post_like_count.text = users.size.toString() // 좋아요 개수 뿌리기
                        if(users.contains(username))
                            image_heart.setColorFilter(Color.parseColor("#FF0000"))
                    }

                    // 댓글 작성해서 DB에 등록
                    submit_btn.setOnClickListener {
                        val rep = reply.editableText.toString()
                        if(rep != ""){
                            postViewModel.setCommentRef(key)
                            val commentKey = postViewModel.getCommentKey()

                            // 사용자 프로필 사진도 DB에 등록해서 가져오기
                            val profile = Uri.parse(postViewModel.getUserImg())
                            val newComment = Comments(profile, username, rep, commentKey)

                            postViewModel.setComment(key, commentKey, newComment)

                            Toast.makeText(applicationContext, "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show()
                            reply.setText(null)
                            closeKeyboard()
                        }
                    }

                    // 좋아요 버튼 기능
                    image_heart.setOnClickListener {
                        if(users.contains(username)){ // 이미 누른 상태에서 다시 누르는 거 -> 회색으로 바꾸고 DB에서 삭제
                            image_heart.setColorFilter(Color.parseColor("#D0CFCF"))
                            users.remove(username)
                            postViewModel.deleteHeart(key, username)
                        }
                        else{
                            image_heart.setColorFilter(Color.parseColor("#FF0000"))
                            postViewModel.addHeart(key, username)
                        }
                    }
                }

                // 게시글 사진 뷰 어댑터
                adapter = PostViewPagerAdapter(cont, uri)
                ViewPager.adapter = adapter
                ViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                // 댓글 리사이클러 뷰 어댑터
                comments.layoutManager = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                commentAdapter = PostCommentAdapter(cmts, postViewModel, username, key, cont, menuInflater)// 현재 게시글의 comment 가져오기
                comments.adapter = commentAdapter

                // 게시글에 등록된 고양이 리사이클러 뷰 어댑터
                catRecyclerView.layoutManager = LinearLayoutManager(cont, LinearLayoutManager.HORIZONTAL, false)
                postCatNameAdapter = PostCatNameAdapter(catList)
                catRecyclerView.adapter = postCatNameAdapter

                postCatNameAdapter.setItemClickListener(object :
                    PostCatNameAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        //intent에 해당 고양이의 database id를 같이 넘겨 보내준다.
                        val intent = Intent(this@PostActivity, CatInfo::class.java)
                        intent.putExtra("catId", catList[position].catid)
                        intent.putExtra("catName", catList[position].name)
                        startActivity(intent)
                    }
                })
            }
        }) // onDataChange


        more_btn.setOnClickListener { // 게시글 삭제하기 버튼 기능
            val popup = PopupMenu(this, it)
            menuInflater.inflate(R.menu.post_delete_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.deletePost -> {
                        println("this")
                        val deleteCheck = AlertDialog.Builder(this)

                        deleteCheck.setTitle("게시글을 삭제하시겠습니까?")
                        deleteCheck.setPositiveButton("삭제"){ dialog, which ->
                            // DB에서 포스트 데이터 삭제
                            postViewModel.deletePost(key)
                            val intent = Intent(cont, MainActivity::class.java)
                            startActivity(intent) // 삭제하면 postFragment로 가게 하고싶은데 activity -> fragment가 되나? 일단은 mainActivity로 이동
                        }
                        deleteCheck.setNegativeButton("취소", null)
                        deleteCheck.show()
                    }
                }
                false
            }
            if(username == postAuthor) // 본인이 쓴 게시글만 삭제할 수 있도록 함
                popup.show()
        }

        // 게시글에서 사진 개수랑 현재 페이지가 몇 번째인지 띄워주는 코드      ->   1 / 3
        ViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 다른 페이지로 스크롤 됐을 때 ViewPager 의 현재 페이지 텍스트뷰를 갱신해준다.
                tv_num.text = (position + 1).toString()
            }
        })
    }

    fun closeKeyboard() { // 댓글 작성하면 키보드 내려주는 코드
        val view = this.currentFocus
        if (view != null) {
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}