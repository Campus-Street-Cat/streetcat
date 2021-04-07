package com.example.streetcat.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.temp.*


class HomeFragment : Fragment() {
    var cats = arrayListOf<list_cats>(list_cats(R.drawable.p1, "폼폼이1"),
            list_cats(R.drawable.p2, "폼폼이2"), list_cats(R.drawable.p3, "폼폼이3"),
            list_cats(R.drawable.p4, "폼폼이4"), list_cats(R.drawable.pompom1, "폼폼이5"),
            list_cats(R.drawable.add, "추가"))
    lateinit var recyclerView1 : RecyclerView
    lateinit var recyclerView2 : RecyclerView

    private val adapter = RecyclerViewAdapter(cats)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        var rootView =  inflater.inflate(R.layout.temp, container, false)

        recyclerView1 = rootView.findViewById(R.id.recyclerView!!)as RecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView1.adapter = adapter

        adapter.setItemClickListener(object : RecyclerViewAdapter.ItemClickListener{
            override fun onClick(view : View, position : Int){
                if(position == 0) {
                    val intent = Intent(context, CatInfo::class.java)
                    startActivity(intent)
                }
                else if(position == 5){
                    val intent = Intent(context, CatAdd::class.java)
                    startActivity(intent)
                }
            }
        })

        // 즐겨찾는 고양이 recyclerView
        recyclerView2 = rootView.findViewById(R.id.recyclerView2!!)as RecyclerView
        recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.adapter = RecyclerViewAdapter(cats)

        return rootView
    }
}