package com.example.streetcat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.streetcat.R

//어플 실행 시 처음 나오는 로딩 화면
class Intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

    //intent를 사용해서 loginactivity로 3초후 이동시킴
      Handler(Looper.getMainLooper()).postDelayed({
          startActivity(Intent(this, LoginActivity::class.java));
          finish();
      }, 1500)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
