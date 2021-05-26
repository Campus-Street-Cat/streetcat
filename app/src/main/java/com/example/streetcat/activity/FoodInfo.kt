package com.example.streetcat.activity

import android.app.TimePickerDialog
import android.content.Context
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

private val catViewModel: CatInfoViewModel by viewModels()

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_food_info)
    setButtonClickEvent()

    btn_upload.setOnClickListener(object : View.OnClickListener { //밥 등록시 처리
        override fun onClick(v: View?) {
            //TODO

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("feeding")
            myRef.setValue("식사")

        //에러
        /*
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    val value = dataSnapshot?.value
                    textView.text = "$value"
                }

                override fun onCancelled(p0: DatabaseError?) {
                    println("Failed to read value.")

                }
            })*/
        }


    })
}
    private fun setButtonClickEvent(){
        btn_morning.setOnClickListener {onCheckedChanged(btn_morning, btn_morning.isChecked)}
        btn_evening.setOnClickListener {onCheckedChanged(btn_evening, btn_evening.isChecked)}
        btn_afternoon.setOnClickListener {onCheckedChanged(btn_afternoon, btn_afternoon.isChecked)}
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

    private fun onCheckedChanged(view: CompoundButton, isChecked: Boolean) {
        when(view){
            btn_morning-> {
                if (isChecked) {
                    var calendar = Calendar.getInstance()
                    var hour = calendar.get(Calendar.HOUR)
                    var minute = calendar.get(Calendar.MINUTE)

                    var listener = TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                        tv_morning.text = "${i}시 ${i2}분"
                    }
                    var picker = TimePickerDialog(this, listener, hour, minute, false)
                    picker.show()
                    Toast.makeText(this, "아침밥을 기록합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            btn_evening -> {

                if (isChecked) {
                    var calendar = Calendar.getInstance()
                    var hour = calendar.get(Calendar.HOUR)
                    var minute = calendar.get(Calendar.MINUTE)

                    var listener = TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                        tv_evening.text = "${i}시 ${i2}분"
                    }
                    var picker = TimePickerDialog(this, listener, hour, minute, false)
                    picker.show()
                    Toast.makeText(this, "점심밥을 기록합니다.", Toast.LENGTH_SHORT).show()
                }

            }

            btn_afternoon -> {
                //TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()\
                if (isChecked) {
                    var calendar = Calendar.getInstance()
                    var hour = calendar.get(Calendar.HOUR)
                    var minute = calendar.get(Calendar.MINUTE)

                    var listener = TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                        tv_afternoon.text = "${i}시 ${i2}분"
                    }
                    var picker = TimePickerDialog(this, listener, hour, minute, false)
                    picker.show()
                    Toast.makeText(this, "저녁밥을 기록합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}