package com.example.streetcat.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R
import kotlinx.android.synthetic.main.activity_food_info.*
import java.text.SimpleDateFormat
import java.util.*

class FoodInfo : AppCompatActivity() {

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_food_info)
    setButtonClickEvent()
}



    private fun setButtonClickEvent(){
        btn_morning.setOnClickListener {onClick(btn_morning)}
        btn_evening.setOnClickListener {onClick(btn_evening)}
        btn_afternoon.setOnClickListener {onClick(btn_afternoon)}
    }

    private fun onClick(view: View) = View.OnClickListener {
        when(view){
            btn_morning-> {
                Toast.makeText(this, "아침밥을 기록합니다.", Toast.LENGTH_SHORT).show()
            }

            btn_evening -> {
                Toast.makeText(this, "점심밥을 기록합니다.", Toast.LENGTH_SHORT).show()
            }

            btn_afternoon -> {
                Toast.makeText(this, "저녁밥을 기록합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}