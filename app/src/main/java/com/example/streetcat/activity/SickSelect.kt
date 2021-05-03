package com.example.streetcat.activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstune.searchablemultiselectspinner.*
import com.example.streetcat.R
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.adapter.SickListAdapter
import com.google.android.play.core.internal.i
import kotlinx.android.synthetic.main.activity_sick_select.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SickSelect : AppCompatActivity() {

    lateinit var adapter : SickListAdapter
    private var items: MutableList<SearchableItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_select)

        //날짜 출력
        val currentTime: Date = Calendar.getInstance().getTime()
        val date_text: String =
            SimpleDateFormat("yyyy년 MM월 dd일 EE요일", Locale.getDefault()).format(currentTime)
        Log.d("webnautes", date_text)
        tv_date.text = date_text


        var data1 = arrayOf("열이 나요", "토를 했어요", "피부병이 있어요", "설사를 했어요", "눈물을 흘려요", "기침을 해요", "잠을 많이 자요")

        for (i in data1) {
            items.add(SearchableItem("증상 $i", i))
        }

        todaySickList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        button.setOnClickListener {
            SearchableMultiSelectSpinner.show(this, "Select Items","Done", items, object :
                SelectionCompleteListener {
                override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                    Log.d("data", selectedItems.toString())
                    adapter = SickListAdapter(selectedItems)
                    todaySickList.adapter = adapter
                }
        })

    }
}}