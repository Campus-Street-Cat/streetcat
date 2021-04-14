package com.example.streetcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import kotlinx.android.synthetic.main.activity_cat_main.*

class CatInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_main)

        gridView.adapter = ImageAdapter(this)
    }
}