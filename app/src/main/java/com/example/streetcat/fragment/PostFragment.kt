package com.example.streetcat.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.*
import com.example.streetcat.activity.CatAdd
import com.example.streetcat.activity.CatInfo
import com.example.streetcat.activity.PostActivity
import com.example.streetcat.activity.WritePost
import com.example.streetcat.adapter.CatInfoGalleryAdapter
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*

class PostFragment : Fragment() {
    private val postViewModel: PostViewModel by viewModels()
    lateinit var adapter: CatInfoGalleryAdapter
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        post_write.setOnClickListener(ButtonListener())

        postViewModel.getPostRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    var flag = true
                    for (comp in postViewModel.getPosts()) {
                        if (comp.key == data.key && comp.photo.isNotEmpty())
                            flag = false
                    }
                    if (flag) {
                        val uris = ArrayList<Uri?>()
                        val cnt = data.child("cnt").value.toString().toInt()

                        for (idx in 0 until cnt) {
                            val v = data.child("pictures").child(idx.toString()).value
                            if (v != null)
                                uris.add(Uri.parse(v.toString()))
                        }

                        val key = data.key.toString()
                        if (uris.isNotEmpty())
                            postViewModel.addPost(uris, key)
                    }
                }

                //Log.d("getPost", postViewModel.getPosts().toString())
                post_gallery.layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = CatInfoGalleryAdapter(postViewModel.getPosts())
                post_gallery.adapter = adapter

                adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        val intent = Intent(context, PostActivity::class.java)
                        intent.putExtra(
                            "postKey",
                            Uri.parse(postViewModel.getPosts()[position].key).toString()
                        ) // 해당 게시글로 갈 수 있도록 key 값을 넘겨서 화면 전환
                        startActivity(intent)
                    }
                })
            }
        })
    }

    // 글쓰기 버튼 리스너
    inner class ButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context, WritePost::class.java)
            startActivity(intent)
        }
    }
}