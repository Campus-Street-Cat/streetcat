package com.example.streetcat.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R

// 설정 -> 공지사항 액티비티
class NoticeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
    }
}