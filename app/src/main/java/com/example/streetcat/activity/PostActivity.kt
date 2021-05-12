package com.example.streetcat.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.streetcat.data.Comments
import com.example.streetcat.R
import com.example.streetcat.adapter.CatInfoGalleryAdapter
import com.example.streetcat.adapter.PostCommentAdapter
import com.example.streetcat.adapter.PostViewPagerAdapter
import com.example.streetcat.viewModel.MainViewModel
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_write_post.*
import kotlinx.android.synthetic.main.fragment_post.*


class PostActivity : AppCompatActivity() {
    lateinit var adapter: PostViewPagerAdapter
    lateinit var commentAdapter : PostCommentAdapter

    private val postViewModel: PostViewModel by viewModels()
    lateinit var key : String

    var imm : InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        val postKey : String? = intent.getStringExtra("postKey")
        val cont = this
        if(postKey != null)
            key = postKey

        // 유저 이름 가져오기
        postViewModel.getUserRef().child("nickName").get().addOnSuccessListener {
            postViewModel.setNickname(it.value.toString())
        }

        postViewModel.getPostRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val uri = ArrayList<Uri>()
                val cmts = ArrayList<Comments>()

                for (data in dataSnapshot.children) {
                    if(data.key == postKey){ // 키 값이 같은 데이터를 찾으면
                        val cnt = data.child("cnt").value.toString() // 사진 개수

                        tv_total.text = cnt

                        user_name.text = data.child("author").value.toString() // 작성자 이름 뿌리기
                        context.text = data.child("contents").value.toString() // 글 본문 내용 뿌리기
                        post_like_count.text = data.child("churu").value.toString() // 좋아요 개수 뿌리기
                        com_count.text = data.child("comments_cnt").value.toString()

                        for(i in 0 until cnt.toInt()){
                            uri.add(Uri.parse(data.child("pictures").child(i.toString()).value.toString())) // 사진 uri
                        }

                        // 해당 게시물의 댓글 가져오기
                        val temp = data.child("comments").children
                        for(cmt in temp){ // 댓글 내용
                            val profile = Uri.parse(cmt.child("userImg").value.toString())
                            val name = cmt.child("username").value.toString()
                            val c = cmt.child("comment").value.toString()
                            val likeCnt = cmt.child("likeCnt").value.toString() // cnt가 int형이면 이상하게 안됨
                            cmts.add(Comments(profile, name, c, likeCnt))
                        }
                    }

                    // 댓글 작성해서 DB에 등록
                    submit_btn.setOnClickListener {
                        postViewModel.setCommentRef(key)
                        val commentKey = postViewModel.getCommentKey()

                        // 사용자 프로필 사진도 DB에 등록해서 가져오기
                        val profile = Uri.parse("https://firebasestorage.googleapis.com/v0/b/streetcat-fd0b0.appspot.com/o/-MZvXYgCftH-88ExGp6g_1.png?alt=media&token=856d791e-2eab-41b3-8d00-00fe005a779b")
                        val name = postViewModel.getNickname()
                        val rep = reply.editableText.toString()
                        val like = "0"
                        val newComment = Comments(profile, name, rep, like)

                        postViewModel.setComment(key, commentKey, newComment)
                        postViewModel.setCommentCnt(key, cmts.size + 1)

                        Toast.makeText(applicationContext, "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show()
                        reply.setText(null)
                        CloseKeyboard()
                    }
                }

                // 게시글 사진 뷰 어댑터
                adapter = PostViewPagerAdapter(cont, uri)
                ViewPager.adapter = adapter
                ViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                // 댓글 리사이클러 뷰 어댑터
                comments.layoutManager = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                commentAdapter = PostCommentAdapter(cmts)// 현재 게시글의 comment 가져오기
                comments.adapter = commentAdapter
            }
        })

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

    fun CloseKeyboard() { // 댓글 작성하면 키보드 내려주는 코드
        val view = this.currentFocus
        if (view != null) {
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}