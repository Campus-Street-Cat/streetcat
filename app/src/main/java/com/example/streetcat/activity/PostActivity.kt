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

    var imm : InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        val postKey : String? = intent.getStringExtra("postKey")
        val cont = this

        postViewModel.getPostRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val uri = ArrayList<Uri>()
                val cmts = ArrayList<Comments>()
                var commentCnt = 0

                for (data in dataSnapshot.children) {
                    if(data.key == postKey){ // 키 값이 같은 데이터를 찾으면
                        val cnt = data.child("cnt").value.toString() // 사진 개수
                        commentCnt = data.child("comments_cnt").value.toString().toInt() // 댓글 개수

                        tv_total.text = cnt

                        user_name.text = data.child("author").value.toString() // 작성자 이름 뿌리기
                        context.text = data.child("contents").value.toString() // 글 본문 내용 뿌리기
                        post_like_count.text = data.child("churu").value.toString() // 좋아요 개수 뿌리기

                        for(i in 0 until cnt.toInt()){
                            uri.add(Uri.parse(data.child("pictures").child(i.toString()).value.toString())) // 사진 uri
                        }

                        // 해당 게시물의 댓글 가져오기
                        // Comments : user_img, username, comment, cnt
                        for(i in 0 until commentCnt){ // 댓글 내용
                            val profile = Uri.parse(data.child("comments").child(i.toString()).child("userImg").value.toString())
                            val name = data.child("comments").child(i.toString()).child("username").value.toString()
                            val c = data.child("comments").child(i.toString()).child("comment").value.toString()
                            val cnt = data.child("comments").child(i.toString()).child("cnt").value.toString().toInt()
                            cmts.add(Comments(profile, name, c, cnt))
                        }
                    }

                    // 댓글 작성해서 DB에 등록
                    submit_btn.setOnClickListener {
                        val profile = Uri.parse("https://firebasestorage.googleapis.com/v0/b/streetcat-fd0b0.appspot.com/o/-MZvXYgCftH-88ExGp6g_1.png?alt=media&token=856d791e-2eab-41b3-8d00-00fe005a779b")
                        val name = "현재 로그인된 유저 닉네임"
                        val rep = reply.editableText.toString()
                        val likeCnt = 0; // 댓글의 좋아요 개수
                        val newComment = Comments(profile, name, rep, likeCnt)

                        if (postKey != null) {
                            Log.d("comment", commentCnt.toString())
                            postViewModel.setComment(postKey, commentCnt, newComment)
                        }
                        Toast.makeText(applicationContext, "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show()
                        reply.setText(null)
                        CloseKeyboard()
                    }
                }

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
        var view = this.currentFocus
        if (view != null) {
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}