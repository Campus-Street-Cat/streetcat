package com.example.streetcat

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : AppCompatActivity() {
    val postImage = arrayListOf<Int> (R.drawable.p1, R.drawable.p2, R.drawable.p3)
    var com = arrayListOf<Comments>(Comments(R.drawable.p1, "kau_sw", "댓글 1"),
        Comments(R.drawable.p2, "kau_pilot", "댓글 2"), Comments(R.drawable.p3, "kau_business", "댓글 3"))

    private val postImage_adapter = ViewPagerAdapter(this, postImage)
    private val comment_adapter = CommentAdapter(com)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        // 게시글에서 사진 보여주는 어댑터
        ViewPager.adapter = postImage_adapter
        ViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 댓글 리사이클러 뷰 어댑터
        comments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        comments.adapter = comment_adapter

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