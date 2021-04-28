package com.example.streetcat.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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
import kotlinx.android.synthetic.main.fragment_post.*


class PostActivity : AppCompatActivity() {
    private val postImage = arrayListOf<Int> (R.drawable.p1, R.drawable.p2, R.drawable.p3)

    val com = arrayListOf<Comments>(
        Comments(R.drawable.p1, "kau_sw", "댓글 1"),
        Comments(R.drawable.p2, "kau_pilot", "댓글 2"), Comments(R.drawable.p3, "kau_business", "댓글 3")
    )

    //private val postImageAdapter = PostViewPagerAdapter(this, postImage)
    lateinit var adapter: PostViewPagerAdapter
    private val commentAdapter = PostCommentAdapter(com)

    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)


        val postKey : String? = intent.getStringExtra("postKey")
        val cont = this

        postViewModel.getPostRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var uri = ArrayList<Uri>()
                for (data in dataSnapshot.children) {
                    if(data.key == postKey){ // 키 값이 같은 데이터를 찾으면
                        user_name.text = data.child("author").value.toString() // 작성자 이름 뿌리기
                        context.text = data.child("contents").value.toString() // 글 본문 내용 뿌리기
                        post_like_count.text = data.child("churu").value.toString() // 좋아요 개수 뿌리기

                        uri.add(Uri.parse(data.child("picture").value.toString())) // 사진 uri
                        // 사진, 댓글 수, 댓글 내용 추가해야함
                    }
                }

                adapter = PostViewPagerAdapter(cont, uri)
                ViewPager.adapter = adapter
                ViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }
        })



        // 게시글에서 사진 보여주는 어댑터
        //ViewPager.adapter = postImageAdapter

        /*ViewPager.adapter = adapter
        ViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL*/


        // 댓글 리사이클러 뷰 어댑터
        comments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        comments.adapter = commentAdapter

        // 게시글에서 사진 개수랑 현재 페이지가 몇 번째인지 띄워주는 코드      ->   1 / 3
        tv_total.text = postImage.size.toString()
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
}