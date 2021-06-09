package com.example.streetcat.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.streetcat.*
import com.example.streetcat.activity.WritePost
import com.example.streetcat.viewModel.PostViewModel
import kotlinx.android.synthetic.main.fragment_post.*

class PostFragment : Fragment() {
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 유저 닉네임 set
        postViewModel.getUserRef().child("nickName").get().addOnSuccessListener {
            postViewModel.setNickname(it.value.toString())
        }
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        // 글쓰기 버튼 리스너
        post_write.setOnClickListener(ButtonListener())
        // postFragment 리사이클러뷰 보여주기
        postViewModel.showPostFragmentRcView(requireContext(), post_gallery, search_view)
    }

    // 글쓰기 버튼 눌렀을 때 WritePost 시작
    inner class ButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context, WritePost::class.java)
            startActivity(intent)
        }
    }
}