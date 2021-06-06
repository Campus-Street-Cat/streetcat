package com.example.streetcat.activity

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R
import com.example.streetcat.viewModel.CatInfoViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cat_add.*
import kotlinx.android.synthetic.main.activity_food_info.*
import java.text.SimpleDateFormat
import java.util.*

class FoodInfo : AppCompatActivity() {

var bobTime: String = ""

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_food_info)
    val catId = intent.getStringExtra("catId")!!
    val catName = intent.getStringExtra("catName")!!
    setButtonClickEvent()

    btn_upload.setOnClickListener {
        onBackPressed()
        FirebaseDatabase.getInstance().getReference("cats").child(catId).child("feeding").setValue(bobTime)
        val intent = Intent(this, CatInfo::class.java)
        intent.putExtra("catId", catId)
        intent.putExtra("catName", catName)
        startActivity(intent)
    }
}
    private fun setButtonClickEvent(){
        btn_morning.setOnClickListener {onCheckedChanged(btn_morning, btn_morning.isChecked)}
        btn_evening.setOnClickListener {onCheckedChanged(btn_evening, btn_evening.isChecked)}
        btn_afternoon.setOnClickListener {onCheckedChanged(btn_afternoon, btn_afternoon.isChecked)}
    }



    private fun onCheckedChanged(view: CompoundButton, isChecked: Boolean) {
        when(view){
            btn_morning-> {
                if (isChecked) {
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR)
                    val minute = calendar.get(Calendar.MINUTE)

                    val listener = TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                        tv_morning.text = "${i}시 ${i2}분"
                        bobTime = "${i}시 ${i2}분"
                    }
                    val picker = TimePickerDialog(this, listener, hour, minute, false)
                    picker.show()
                    Toast.makeText(this, "아침밥을 기록합니다", Toast.LENGTH_SHORT).show()
                }
            }

            btn_evening -> {

                if (isChecked) {
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR)
                    val minute = calendar.get(Calendar.MINUTE)

                    val listener = TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                        tv_evening.text = "${i}시 ${i2}분"
                        bobTime = "${i}시 ${i2}분"
                    }
                    val picker = TimePickerDialog(this, listener, hour, minute, false)
                    picker.show()
                    Toast.makeText(this, "점심밥을 기록합니다", Toast.LENGTH_SHORT).show()
                }

            }

            btn_afternoon -> {
                //TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()\
                if (isChecked) {
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR)
                    val minute = calendar.get(Calendar.MINUTE)

                    val listener = TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                        tv_afternoon.text = "${i}시 ${i2}분"
                        bobTime = "${i}시 ${i2}분"
                    }
                    val picker = TimePickerDialog(this, listener, hour, minute, false)
                    picker.show()
                    Toast.makeText(this, "저녁밥을 기록합니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}