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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        //홈 화면 --> 학교 이름에 따라 화면 변화
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
        //고양이 추가하기 버튼 리스너
        add_btn.setOnClickListener(AddBtnListener())
        //랜덤 고양이 보여주기 버튼 리스너
        rand_btn.setOnClickListener(RandBtnListener())
        //학교, 즐겨찾기 고양이 보여주기
        homeViewModel.showHomeRcViews(requireContext(), univ_cats_view, favorite_cats_view)
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
            homeViewModel.showRandomCat(context!!)
        }

    }
}