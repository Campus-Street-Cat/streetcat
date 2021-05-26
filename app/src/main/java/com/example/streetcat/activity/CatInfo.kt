package com.example.streetcat.activity

import android.content.Intent
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
import kotlinx.android.synthetic.main.fragment_home.*

class CatInfo : AppCompatActivity() {
    private val catViewModel: CatInfoViewModel by viewModels()
    lateinit var catId : String
    /*var images = arrayListOf<GalleryPhoto>(
        GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6),
            GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6)
    )*/
   // private val adapter = CatInfoGalleryAdapter(images)

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
                Picasso.get().load(Uri.parse(it.child("picture").value.toString())).into(cat_profile_image);
            }
        }

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

        btn_feed.setOnClickListener{
            val intent = Intent(this, FoodInfo::class.java)
            startActivity(intent)
        }
    }
}