package com.example.streetcat.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.R
import com.example.streetcat.adapter.CatInfoGalleryAdapter
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.data.GalleryPhoto
import com.example.streetcat.viewModel.CatInfoViewModel
import com.example.streetcat.viewModel.HomeViewModel
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cat_main.*
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_post.*

class CatInfo : AppCompatActivity() {
    private val catViewModel: CatInfoViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()
    lateinit var adapter: CatInfoGalleryAdapter
    lateinit var catId : String
    lateinit var catName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_main)

        if(intent.hasExtra("catId")){
            catId = intent.getStringExtra("catId")!!
            catName = intent.getStringExtra("catName")!!
            //고양이 정보 등록
            catViewModel.getCatRef(catId).get().addOnSuccessListener {
                school_name.text = it.child("school").value.toString()
                cat_name.text = it.child("name").value.toString()
                sick_name.text = it.child("sick").value.toString()
                if(sick_name.text == "null") sick_name.text = "정상"
                if(sick_name.text == "정상") {
                    siren.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext, // Context
                            R.drawable.ic_cat_smile // Drawable
                        ))
                    sick_name.setTextColor(Color.BLACK)
                }
                else{
                    siren.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext, // Context
                            R.drawable.siren // Drawable
                        ))
                    sick_name.setTextColor(Color.RED)
                }
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
                        catViewModel.addCat(catId, catName)
                    }
                }
            }
        })

        postViewModel.getUserRef().child("nickName").get().addOnSuccessListener {
            postViewModel.setNickname(it.value.toString())
        }

        postViewModel.getPostRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val catPosts = ArrayList<GalleryPhoto>()

                for (data in dataSnapshot.children) {
                    val cats = ArrayList<String>()
                    val uris = ArrayList<Uri?>()

                    val temp = data.child("cats").children
                    for (cat in temp) {
                        cats.add(cat.key.toString())
                    }

                    val cnt = data.child("cnt").value.toString().toInt()

                    for (idx in 0 until cnt) {
                        val v = data.child("pictures").child(idx.toString()).value
                        if (v != null)
                            uris.add(Uri.parse(v.toString()))
                    }
                    if (cats.contains(catName)) {
                        catPosts.add(GalleryPhoto(uris, data.key.toString()))
                    }
                }
                galleryView.layoutManager = GridLayoutManager(this@CatInfo, 3)
                adapter = CatInfoGalleryAdapter(catPosts)
                galleryView.adapter = adapter

                adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        val intent = Intent(this@CatInfo, PostActivity::class.java)
                        intent.putExtra("postKey", Uri.parse(catPosts[position].key).toString()) // 해당 게시글로 갈 수 있도록 key 값을 넘겨서 화면 전환
                        intent.putExtra("username", postViewModel.getNickname())
                        startActivity(intent)
                    }
                })
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
            intent.putExtra("catName", catName)
            startActivity(intent)
        }

        btn_feed.setOnClickListener{
            val intent = Intent(this, FoodInfo::class.java)
            startActivity(intent)
        }
    }
}