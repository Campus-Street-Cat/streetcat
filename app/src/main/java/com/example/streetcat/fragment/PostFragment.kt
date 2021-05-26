package com.example.streetcat.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
    private val searchList = ArrayList<GalleryPhoto>()
    lateinit var adapter: CatInfoGalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        post_write.setOnClickListener(ButtonListener())

        postViewModel.getUserRef().child("nickName").get().addOnSuccessListener {
            postViewModel.setNickname(it.value.toString())
        }

        postViewModel.getPostRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val uris = ArrayList<Uri?>()
                    var flag = true
                    for (comp in postViewModel.getPosts()) {
                        if (comp.key == data.key && comp.photo.isNotEmpty())
                            flag = false
                    }
                    if (flag) {
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

                post_gallery.layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = CatInfoGalleryAdapter(postViewModel.getPosts())
                post_gallery.adapter = adapter

                search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText == "") {
                            adapter = CatInfoGalleryAdapter(postViewModel.getPosts())
                            post_gallery.adapter = adapter
                            searchList.clear()

                            adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    moveToPost(position, postViewModel.getPosts())
                                }
                            })
                        }
                        return true
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        for (data in dataSnapshot.children) {
                            val cats = ArrayList<String>()
                            val uris = ArrayList<Uri?>()
                            val school = data.child("school").value.toString()
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

                            if (query != null && cats.contains(query) || school.contains(query.toString())) {
                                searchList.add(GalleryPhoto(uris, data.key.toString()))
                            }
                        }
                        adapter = CatInfoGalleryAdapter(searchList)
                        post_gallery.adapter = adapter

                        adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                            override fun onClick(view: View, position: Int) {
                                moveToPost(position, searchList)
                            }
                        })

                        return false
                    }
                })



                adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        moveToPost(position, postViewModel.getPosts())
                    }
                })
            }
        })
    }

    fun moveToPost(position : Int, array : ArrayList<GalleryPhoto>){
        val intent = Intent(context, PostActivity::class.java)
        intent.putExtra("postKey", Uri.parse(array[position].key).toString()) // 해당 게시글로 갈 수 있도록 key 값을 넘겨서 화면 전환
        intent.putExtra("username", postViewModel.getNickname())
        startActivity(intent)
    }

    // 글쓰기 버튼 리스너
    inner class ButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context, WritePost::class.java)
            startActivity(intent)
        }
    }
}