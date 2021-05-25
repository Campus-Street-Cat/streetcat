package com.example.streetcat.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.R
import com.example.streetcat.adapter.CatInfoGalleryAdapter
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.data.GalleryPhoto
import com.example.streetcat.viewModel.CatInfoViewModel
import com.example.streetcat.viewModel.HomeViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cat_main.*
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_home.*

class CatInfo : AppCompatActivity() {
    private val catViewModel: CatInfoViewModel by viewModels()
    lateinit var catId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_main)

        if(intent.hasExtra("catId")){
            catId = intent.getStringExtra("catId")!!
            Log.d("고양이 아이디", catId)
            //고양이 정보 등록
            catViewModel.getCatRef(catId).get().addOnSuccessListener {
                school_name.text = it.child("school").value.toString()
                cat_name.text = it.child("name").value.toString()
                Picasso.get().load(Uri.parse(it.child("picture").value.toString())).into(cat_profile_image)
            }
        }

        catViewModel.getUserRef().addValueEventListener(object : ValueEventListener{ // 사용자가 고양이 즐겨찾기 등록을 했는지 안했는지
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(datasnapshot: DataSnapshot) {
                val cats = ArrayList<String>()

                for(cat in datasnapshot.child("cats").children){
                    cats.add(cat.key.toString())
                }

                if(cats.contains(catId)){ // 이미 좋아요를 누른 상태면 노란색
                    cat_star.setColorFilter(Color.parseColor("#FFCD28"))
                }

                cat_star.setOnClickListener {
                    if(cats.contains(catId)){ // 이미 누른 상태에서 다시 누르는 거 -> 회색으로 바꾸고 DB에서 삭제
                        cat_star.setColorFilter(Color.parseColor("#D0CFCF"))
                        cats.remove(catId)
                        catViewModel.deleteCat(catId)
                    }
                    else{
                        cat_star.setColorFilter(Color.parseColor("#FFCD28"))
                        catViewModel.addCat(catId)
                    }
                }

            }
        })


        detail_info.setOnClickListener{
            val intent = Intent(this, CatDetailInfo::class.java)
            intent.putExtra("catId", catId)
            startActivity(intent)
        }

        cat_health.setOnClickListener{
            val intent = Intent(this, SickInfo::class.java)
            intent.putExtra("catId", catId)
            startActivity(intent)
        }
    }
}