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
import com.example.streetcat.data.Cat
import com.example.streetcat.viewModel.HomeViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    lateinit var schoolAdapter: HomeRecyclerViewAdapter
    lateinit var favoriteAdapter: HomeRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel.getUserRef().child("schoolName").get().addOnSuccessListener {
            homeViewModel.setSchoolName(it.value.toString())
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_btn.setOnClickListener(ButtonListener())

        val userCat = ArrayList<Cat>()


        homeViewModel.getUserRef().child("schoolName").get().addOnSuccessListener {
            homeViewModel.setSchoolName(it.value.toString())

            // dbViewModel 의 cats 배열 observing
            homeViewModel.getCatRef().addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (data in p0.children) {
                        val cat = data.value.toString()
                        homeViewModel.getCatRef(cat).get().addOnSuccessListener {
                            var flag = true
                            for (comp in homeViewModel.getCats()) {
                                if (comp.name == it.child("name").value.toString()) flag = false
                            }
                            if (flag) {
                                homeViewModel.addCat(Uri.parse(it.child("picture").value.toString()), it.child("name").value.toString(), cat)

                                // 학교 고양이 리사이클러뷰
                                univ_cats_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
                                schoolAdapter = HomeRecyclerViewAdapter(homeViewModel.getCats())
                                univ_cats_view.adapter = schoolAdapter

                                schoolAdapter.setItemClickListener(object :
                                    HomeRecyclerViewAdapter.ItemClickListener {
                                    override fun onClick(view: View, position: Int) {
                                        //intent에 해당 고양이의 database id를 같이 넘겨 보내준다.
                                        val intent = Intent(context, CatInfo::class.java)
                                        intent.putExtra("catId", homeViewModel.getCats()[position].catid)
                                        intent.putExtra("catName", homeViewModel.getCats()[position].name)
                                        startActivity(intent)
                                    }
                                })
                            }

                            homeViewModel.getUserRef().addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(data: DataSnapshot) {
                                    val userCatKey = ArrayList<String>()
                                    val temp = data.child("cats").children
                                    val tempCat = Cat(Uri.parse(it.child("picture").value.toString()), it.child("name").value.toString(), cat)

                                    for(cat in temp){
                                        if(!userCatKey.contains(cat.key.toString()))
                                            userCatKey.add(cat.key.toString())
                                    }

                                    if(userCatKey.contains(it.key.toString()) && !userCat.contains(tempCat)){
                                        userCat.add(tempCat)
                                    }
                                    else if(!userCatKey.contains(it.key.toString()) && userCat.contains(tempCat)){
                                        userCat.remove(tempCat)
                                    }


                                    // 즐겨찾는 고양이 리사이클러뷰
                                    favorite_cats_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
                                    favoriteAdapter = HomeRecyclerViewAdapter(userCat)
                                    favorite_cats_view.adapter = favoriteAdapter

                                    favoriteAdapter.setItemClickListener(object :
                                        HomeRecyclerViewAdapter.ItemClickListener {
                                        override fun onClick(view: View, position: Int) {
                                            //intent에 해당 고양이의 database id를 같이 넘겨 보내준다.
                                            val intent = Intent(context, CatInfo::class.java)
                                            intent.putExtra("catId", userCat[position].catid)
                                            intent.putExtra("catName", userCat[position].name)
                                            startActivity(intent)
                                        }
                                    })
                                }
                            })
                        }
                    }
                }
            })
        }
    }

    // 고양이 추가 버튼 누를 시
    inner class ButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context, CatAdd::class.java)
            startActivity(intent)
        }
    }
}