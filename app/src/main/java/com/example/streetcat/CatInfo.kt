package com.example.streetcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cat_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class CatInfo : AppCompatActivity() {
    var images = arrayListOf<GalleryPhoto>(GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6),
            GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6))
    private val adapter = GalleryAdapter(images)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_main)

        //gallaryView.adapter = ImageAdapter(this)

        galleryView.layoutManager = GridLayoutManager(this, 3)
        galleryView.adapter = adapter
    }
}