package com.example.streetcat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.streetcat.R

class Intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

//intent를 사용해서 mainactivity로 3초후 이동시킴
        var handler = Handler()
        handler.postDelayed( {
            var intent = Intent( this, MainActivity::class.java)
            startActivity(intent)
        }, 3000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
