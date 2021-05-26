package com.example.streetcat.activity

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
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

    fun getTime(button: Button, context: Context){ //time pick 스피너

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            button.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

    }

    private fun onClick(view: View) = View.OnClickListener {
        when(view){
            btn_morning-> {
                var calendar = Calendar.getInstance()
                var hour = calendar.get(Calendar.HOUR)
                var minute = calendar.get(Calendar.MINUTE)

                var listener = TimePickerDialog.OnTimeSetListener{_, i, i2->
                    tv_morning.text = "${i}시 ${i2}분"
                }
                var picker = TimePickerDialog(this, listener, hour, minute, false)
                picker.show()
                Toast.makeText(this, "아침밥을 기록합니다.", Toast.LENGTH_SHORT).show()
            }

            btn_evening -> {
                Toast.makeText(this, "점심밥을 기록합니다.", Toast.LENGTH_SHORT).show()
            }

            btn_afternoon -> {
                //TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()\
                Toast.makeText(this, "저녁밥을 기록합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}