package com.example.streetcat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.streetcat.R
import com.example.streetcat.adapter.CatInfoGalleryAdapter
import com.example.streetcat.data.GalleryPhoto
import kotlinx.android.synthetic.main.activity_cat_main.*

class CatInfo : AppCompatActivity() {
    /*var images = arrayListOf<GalleryPhoto>(
        GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6),
            GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6)
    )*/
   // private val adapter = CatInfoGalleryAdapter(images)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_main)

        /*galleryView.layoutManager = GridLayoutManager(this, 3)
        galleryView.adapter = adapter

        detail_info.setOnClickListener{
            val intent = Intent(this, CatDetailInfo::class.java)
            startActivity(intent)
        }

        cat_health.setOnClickListener{
            val intent = Intent(this, SickInfo::class.java)
            startActivity(intent)
        }*/
    }
}