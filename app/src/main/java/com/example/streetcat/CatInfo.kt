package com.example.streetcat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_cat_main.*
import kotlinx.android.synthetic.main.activity_post.*

class CatInfo : AppCompatActivity() {
    var images = arrayListOf<GalleryPhoto>(GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6),
            GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6))
    private val adapter = GalleryAdapter(images)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_main)

        galleryView.layoutManager = GridLayoutManager(this, 3)
        galleryView.adapter = adapter

        detail_info.setOnClickListener{
            val intent = Intent(this, CatDetailInfo::class.java)
            startActivity(intent)
        }
    }
}