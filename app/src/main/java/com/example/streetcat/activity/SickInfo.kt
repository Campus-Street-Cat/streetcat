package com.example.streetcat.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.R
import com.example.streetcat.adapter.SickListAdapter
import com.example.streetcat.data.SickList
import com.example.streetcat.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_cat_main.*
import kotlinx.android.synthetic.main.activity_food_info.*
import kotlinx.android.synthetic.main.activity_sick_info.*
import kotlinx.android.synthetic.main.delete_check.*
import java.util.*

class SickInfo : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    lateinit var sickName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_info)

        val catId = intent.getStringExtra("catId")!!
        val catName = intent.getStringExtra("catName")!!

        btn_sickinfo.setOnClickListener{
            if(sickCheckBox1.isChecked || sickCheckBox2.isChecked || sickCheckBox3.isChecked || sickCheckBox4.isChecked || sickCheckBox0.isChecked){
                if(sickCheckBox0.isChecked) sickName = "정상"
                else if(sickCheckBox1.isChecked) sickName = sickname1.text.toString()
                else if(sickCheckBox2.isChecked) sickName = sickname2.text.toString()
                else if(sickCheckBox3.isChecked) sickName = sickname3.text.toString()
                else sickName = sickname4.text.toString()

                homeViewModel.setCatSick(catId, sickName)
                Toast.makeText(applicationContext, "이상 증상이 등록되었습니다", Toast.LENGTH_SHORT).show()
                onBackPressed()
                val intent = Intent(this, CatInfo::class.java)
                intent.putExtra("catId", catId)
                intent.putExtra("catName", catName)
                startActivity(intent)

            }
            else{
                Toast.makeText(this, "이상 증상을 체크해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}