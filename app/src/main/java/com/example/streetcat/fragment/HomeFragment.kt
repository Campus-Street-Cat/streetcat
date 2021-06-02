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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList


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
            univ_text.text = it.value.toString()
            when(it.value.toString()){
                "한국항공대학교" -> Picasso.get().load(R.drawable.kau).error(R.drawable.common_google_signin_btn_icon_dark).into(univ_logo)
                "서울대학교" -> Picasso.get().load(R.drawable.seoul).error(R.drawable.common_google_signin_btn_icon_dark).into(univ_logo)
                "KAIST" -> Picasso.get().load(R.drawable.kaist).error(R.drawable.common_google_signin_btn_icon_dark).into(univ_logo)
            }
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        add_btn.setOnClickListener(AddBtnListener())
        rand_btn.setOnClickListener(RandBtnListener())


        homeViewModel.getUserRef().addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(data: DataSnapshot) {
                val userCat = ArrayList<Cat>()
                val temp = data.child("cats").children
                for(cat in temp){
                    userCat.add(Cat(Uri.parse(""), cat.value.toString(), cat.key.toString()))
                }


                // 고양이 참조 시작
                homeViewModel.getAllCatRef().addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val data = dataSnapshot.children
                        val schoolCat = ArrayList<Cat>()
                        for(cat in data){
                            val catSchool = cat.child("school").value.toString()
                            if(catSchool == homeViewModel.getSchool()){
                                val img = Uri.parse(cat.child("picture").value.toString())
                                val name = cat.child("name").value.toString()
                                schoolCat.add(Cat(img, name, cat.key.toString()))
                            } // 학교 고양이 등록

                            for(usercat in userCat){
                                if(cat.key.toString() == usercat.catid){
                                    usercat.img = Uri.parse(cat.child("picture").value.toString())
                                }
                            }


                            // 학교 고양이 리사이클러뷰
                            univ_cats_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            schoolAdapter = HomeRecyclerViewAdapter(schoolCat)
                            univ_cats_view.adapter = schoolAdapter

                            schoolAdapter.setItemClickListener(object : HomeRecyclerViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val intent = Intent(context, CatInfo::class.java)
                                    intent.putExtra("catId", schoolCat[position].catid)
                                    intent.putExtra("catName", schoolCat[position].name)
                                    startActivity(intent)
                                }
                            })

                            // 즐겨찾는 고양이 리사이클러뷰
                            favorite_cats_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            favoriteAdapter = HomeRecyclerViewAdapter(userCat)
                            favorite_cats_view.adapter = favoriteAdapter

                            favoriteAdapter.setItemClickListener(object :
                                HomeRecyclerViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val intent = Intent(context, CatInfo::class.java)
                                    intent.putExtra("catId", userCat[position].catid)
                                    intent.putExtra("catName", userCat[position].name)
                                    startActivity(intent)
                                }
                            })

                        }
                    } // onDataChange
                }) // AllCatRef
            } // onDataChange
        }) // UserRef

//        homeViewModel.getUserRef().child("schoolName").get().addOnSuccessListener {
//            homeViewModel.setSchoolName(it.value.toString())
//            univ_text.text = it.value.toString()
//            when(it.value.toString()){
//                "한국항공대학교" -> Picasso.get().load(R.drawable.kau).error(R.drawable.common_google_signin_btn_icon_dark).into(univ_logo)
//                "서울대학교" -> Picasso.get().load(R.drawable.seoul).error(R.drawable.common_google_signin_btn_icon_dark).into(univ_logo)
//                "KAIST" -> Picasso.get().load(R.drawable.kaist).error(R.drawable.common_google_signin_btn_icon_dark).into(univ_logo)
//            }
//
//            // dbViewModel 의 cats 배열 observing
//            homeViewModel.getCatRef().addValueEventListener(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onDataChange(p0: DataSnapshot) {
//                    for (data in p0.children) {
//                        val cat = data.value.toString()
//                        homeViewModel.getCatRef(cat).get().addOnSuccessListener {
//                            var flag = true
//                            for (comp in homeViewModel.getCats()) {
//                                if (comp.name == it.child("name").value.toString()) flag = false
//                            }
//                            if (flag) {
//                                homeViewModel.addCat(
//                                    Uri.parse(it.child("picture").value.toString()),
//                                    it.child("name").value.toString(),
//                                    cat
//                                )
//
//                                // 학교 고양이 리사이클러뷰
//                                univ_cats_view.layoutManager = LinearLayoutManager(
//                                    requireContext(),
//                                    LinearLayoutManager.HORIZONTAL,
//                                    false
//                                )
//                                schoolAdapter = HomeRecyclerViewAdapter(homeViewModel.getCats())
//                                univ_cats_view.adapter = schoolAdapter
//
//                                schoolAdapter.setItemClickListener(object :
//                                    HomeRecyclerViewAdapter.ItemClickListener {
//                                    override fun onClick(view: View, position: Int) {
//                                        //intent에 해당 고양이의 database id를 같이 넘겨 보내준다.
//                                        val intent = Intent(context, CatInfo::class.java)
//                                        intent.putExtra(
//                                            "catId",
//                                            homeViewModel.getCats()[position].catid
//                                        )
//                                        intent.putExtra(
//                                            "catName",
//                                            homeViewModel.getCats()[position].name
//                                        )
//                                        startActivity(intent)
//                                    }
//                                })
//                            }
//
//                            homeViewModel.getUserRef()
//                                .addValueEventListener(object : ValueEventListener {
//                                    override fun onCancelled(error: DatabaseError) {
//                                        TODO("Not yet implemented")
//                                    }
//
//                                    override fun onDataChange(data: DataSnapshot) {
//                                        val userCatKey = ArrayList<String>()
//                                        val temp = data.child("cats").children
//                                        val tempCat = Cat(
//                                            Uri.parse(it.child("picture").value.toString()),
//                                            it.child("name").value.toString(),
//                                            cat
//                                        )
//
//                                        for (cats in temp) {
//                                            if (!userCatKey.contains(cats.key.toString())) {
//                                                userCatKey.add(cats.key.toString())
//                                            }
//                                        }
//
//                                        if (userCatKey.contains(it.key.toString()) && !userCat.contains(
//                                                tempCat
//                                            )
//                                        ) {
//                                            userCat.add(tempCat)
//                                        } else if (!userCatKey.contains(it.key.toString()) && userCat.contains(
//                                                tempCat
//                                            )
//                                        ) {
//                                            userCat.remove(tempCat)
//                                        }
//
//                                        // 즐겨찾는 고양이 리사이클러뷰
//                                        favorite_cats_view.layoutManager = LinearLayoutManager(
//                                            requireContext(),
//                                            LinearLayoutManager.HORIZONTAL,
//                                            false
//                                        )
//                                        favoriteAdapter = HomeRecyclerViewAdapter(userCat)
//                                        favorite_cats_view.adapter = favoriteAdapter
//
//                                        favoriteAdapter.setItemClickListener(object :
//                                            HomeRecyclerViewAdapter.ItemClickListener {
//                                            override fun onClick(view: View, position: Int) {
//                                                //intent에 해당 고양이의 database id를 같이 넘겨 보내준다.
//                                                val intent = Intent(context, CatInfo::class.java)
//                                                intent.putExtra("catId", userCat[position].catid)
//                                                intent.putExtra("catName", userCat[position].name)
//                                                startActivity(intent)
//                                            }
//                                        })
//
//                                    }
//                                })
//
//
//                        }
//                    }
//                }
//            })
//        }


    }

    // 고양이 추가 버튼 누를 시
    inner class AddBtnListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context, CatAdd::class.java)
            startActivity(intent)
        }
    }

    // 랜덤 고양이 버튼 누를 시
    inner class RandBtnListener : View.OnClickListener {
        override fun onClick(v: View?) {

            homeViewModel.getAllCatRef().addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val num = p0.childrenCount.toInt()
                    val random = Random()
                    val randNum = random.nextInt(num)

                    var cnt = 0

                    for (data in p0.children) {
                        if (cnt == randNum) {
                            val cat = data.key.toString()
                            homeViewModel.getCatRef(cat).get().addOnSuccessListener {
                                val intent = Intent(context, CatInfo::class.java)
                                intent.putExtra("catId", it.key.toString())
                                intent.putExtra("catName", it.child("name").value.toString())
                                startActivity(intent)
                            }
                        }

                        cnt++
                    }
                }
            })
        }

    }
}