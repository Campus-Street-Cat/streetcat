package com.example.streetcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView

class CatInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_main)

        var item = arrayOf("사진1", "사진2", "사진3", "사진4", "사진5", "사진6", "사진7", "사진8", "사진9", "사진10", "사진11", "사진12") // 임시용
        var list : GridView = findViewById(R.id.list)
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, item)
        list.setAdapter(adapter)
    }
}