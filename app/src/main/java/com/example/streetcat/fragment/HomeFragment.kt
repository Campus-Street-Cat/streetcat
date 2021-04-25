package com.example.streetcat.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.data.Cat
import com.example.streetcat.R
import com.example.streetcat.activity.CatAdd
import com.example.streetcat.activity.CatInfo
import com.example.streetcat.viewModel.FbViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    private val fbViewModel: FbViewModel by viewModels()
    lateinit var adapter: HomeRecyclerViewAdapter
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 고양이 추가 버튼 누를 시
        add_btn.setOnClickListener {
            val intent = Intent(context, CatAdd::class.java)
            startActivity(intent)
        }


        // dbViewModel 의 cats 배열 observing
        fbViewModel.getCatRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    var flag = true
                    for(comp in fbViewModel.getCats()){
                        if(comp.name == data.child("name").value.toString()) flag = false
                    }
                    if(flag) fbViewModel.addCat(Uri.parse(data.child("name").value.toString()), data.child("name").value.toString())
                }
                univ_cats_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = HomeRecyclerViewAdapter(fbViewModel.getCats())
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
        })

        adapter = HomeRecyclerViewAdapter(fbViewModel.getCats())
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