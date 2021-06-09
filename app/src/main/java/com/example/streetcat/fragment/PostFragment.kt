package com.example.streetcat.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.streetcat.*
import com.example.streetcat.activity.PostActivity
import com.example.streetcat.activity.WritePost
import com.example.streetcat.adapter.CatInfoGalleryAdapter
import com.example.streetcat.data.GalleryPhoto
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_post.*

class PostFragment : Fragment() {
    private val postViewModel: PostViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postViewModel.getUserRef().child("nickName").get().addOnSuccessListener {
            postViewModel.setNickname(it.value.toString())
        }
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        post_write.setOnClickListener(ButtonListener())

        postViewModel.showPostFragmentRcView(requireContext(), post_gallery, search_view)
    }



    // 글쓰기 버튼 리스너
    inner class ButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context, WritePost::class.java)
            startActivity(intent)
        }
    }
}