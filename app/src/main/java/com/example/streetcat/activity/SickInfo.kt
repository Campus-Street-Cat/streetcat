package com.example.streetcat.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.R
import com.example.streetcat.adapter.SickListAdapter
import com.example.streetcat.data.SickList
import kotlinx.android.synthetic.main.activity_sick_info.*


class SickInfo : AppCompatActivity() {
    lateinit var sickAdapter : SickListAdapter
    lateinit var sickName : String
    private val sickLists : ArrayList<SickList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_info)

        sickLists.add(SickList("구토를 한다", "몇차례 심하게 토했다"))
        sickLists.add(SickList("설사를 한다", "심한 설사를 한다"))
        sickLists.add(SickList("혈변을 한다", "변에 피가 묻어 나온다"))
        sickLists.add(SickList("자꾸 귀를 문지른다", "귀 뒤쪽에 빨갛고 검은 귀지가 보인다"))
        sickLists.add(SickList("자꾸 몸을 긁는다", "몸에 빨갛게 올라온 염증이 보인다"))


        sick_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        sickAdapter = SickListAdapter(sickLists)
        sick_recyclerView.adapter = sickAdapter
        Log.d("DS","되긴하나..")

    }

}