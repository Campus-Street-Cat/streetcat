package com.example.streetcat.Fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R
import kotlinx.android.synthetic.main.activity_sick_info.*


class MainActivity : AppCompatActivity() {
    //
    var data1 = arrayOf("열이 나요", "토를 했어요", "피부병이 있어요", "설사를 했어요", "눈물을 흘려요")
    var data2 = arrayOf("열이 나요", "토를 했어요", "피부병이 있어요", "설사를 했어요", "눈물을 흘려요")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, data1)
        var adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item,data2)

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter1
        spinner.adapter = adapter2

        val listener = SpinnerListener();
        spinner.onItemSelectedListener = listener //리스너를 넣어줌

        //익명클래스로
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                tv_health1.text = data1[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //버튼 누르면 값 출력
        button.setOnClickListener{view ->
            tv_health2.text = data1[spinner.selectedItemPosition] + "\n"
            tv_health2.append(data2[spinner2.selectedItemPosition])
        }
    }
    inner class SpinnerListener : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            tv_health2.text = data1[position]
        }

        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            TODO("Not yet implemented")
        }


    }
}