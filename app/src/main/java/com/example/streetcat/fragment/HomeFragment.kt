package com.example.streetcat.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.R
import com.example.streetcat.activity.CatAdd
import com.example.streetcat.activity.CatInfo
import com.example.streetcat.activity.SickSelect
import com.example.streetcat.activity.WritePost
import com.example.streetcat.data.Cat
import com.example.streetcat.viewModel.MainViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_post.*


class HomeFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()
    lateinit var adapter: HomeRecyclerViewAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        mainViewModel.getUserRef().child("schoolName").get().addOnSuccessListener {
            mainViewModel.setSchoolName(it.value.toString())
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_btn.setOnClickListener(ButtonListener())

        mainViewModel.getUserRef().child("schoolName").get().addOnSuccessListener {
            mainViewModel.setSchoolName(it.value.toString())

        // dbViewModel 의 cats 배열 observing
        mainViewModel.getCatRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("불러오기 성공", "cancel")
                for (data in p0.children) {
                    Log.d("자식도 있음", data.value.toString())
                    val cat = data.value.toString()
                    mainViewModel.tmp(cat).get().addOnSuccessListener {
                        var flag = true
                        Log.d("인식도 함", it.child("name").value.toString())
                        for(comp in mainViewModel.getCats()){
                            if(comp.name == it.child("name").value.toString()) flag = false
                        }
                        if(flag) {
                            Log.d("추가도 됨", it.child("picture").value.toString())
                            mainViewModel.addCat(
                                Uri.parse(it.child("picture").value.toString()),
                                it.child("name").value.toString()
                            )

                            favorite_cats_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            adapter = HomeRecyclerViewAdapter(mainViewModel.getCats())
                            favorite_cats_view.adapter = adapter
                            univ_cats_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            adapter = HomeRecyclerViewAdapter(mainViewModel.getCats())
                            univ_cats_view.adapter = adapter

                            adapter.setItemClickListener(object : HomeRecyclerViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    if (position == 0) {
                                        val intent = Intent(context, CatInfo::class.java)
                                        startActivity(intent)
                                    } else if (position == 5) {
                                        val intent = Intent(context, CatAdd::class.java)
                                        startActivity(intent)
                                    }
                                }
                            })
                        }
                    }
                }

            }
        })
    }
    }

    // 고양이 추가 버튼 누를 시
    inner class ButtonListener : View.OnClickListener{
        override fun onClick(v: View?) {
            val intent = Intent(context, CatAdd::class.java)
            startActivity(intent)
        }


    }



}