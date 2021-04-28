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
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.data.GalleryPhoto
import com.example.streetcat.data.Post
import com.example.streetcat.viewModel.FbViewModel
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*


/*class PostFragment : Fragment() {
    /*val images = arrayListOf<GalleryPhoto>(
        GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6),
            GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6),
            GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6),
            GalleryPhoto(R.drawable.p1), GalleryPhoto(R.drawable.p2), GalleryPhoto(R.drawable.p3),
            GalleryPhoto(R.drawable.p4), GalleryPhoto(R.drawable.pompom1), GalleryPhoto(R.drawable.p6)
    )*/

    val images = arrayListOf<GalleryPhoto>(GalleryPhoto("https://firebasestorage.googleapis.com/v0/b/streetcat-fd0b0.appspot.com/o?name=-MZCHF14ZLnATB_Vx-wv%2Fmain%2F-MZCHF14ZLnATB_Vx-wv.png&uploadType=resumable&upload_id=ABg5-UxMvWoNvFP31BjPrxOVFx_Rb-YtCJSopzpwpp6v9sMkQeqH4KyGHepvd0xmOomgabm-jGtckQjYiWHnEkRGvQ&upload_protocol=resumable"))
    private val adapter = CatInfoGalleryAdapter(images)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView =  inflater.inflate(R.layout.fragment_post, container, false)

        rootView.post_gallery.layoutManager = GridLayoutManager(requireContext(), 3)
        rootView.post_gallery.adapter = adapter

        adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener{
            override fun onClick(view : View, position : Int){
                if(position == 0) {
                    val intent = Intent(context, PostActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        rootView.post_write.setOnClickListener(ButtonListener())

        return rootView
    }

    inner class ButtonListener : View.OnClickListener{
        override fun onClick(v: View?) {
            val intent = Intent(context, WritePost::class.java)
            startActivity(intent)
        }
    }
}*/

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
                    for(comp in postViewModel.getPosts()){
                        if(comp.photo.toString() == data.child("picture").value.toString())
                            flag = false
                    }
                    if(flag && data.child("picture").value != null) // 이유는 모르겠는데 자꾸 null이 하나 더 들어가서 추가함..
                        postViewModel.addPost(Uri.parse(data.child("picture").value.toString()), data.key.toString())
                }

                post_gallery.layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = CatInfoGalleryAdapter(postViewModel.getPosts())
                post_gallery.adapter = adapter

                adapter.setItemClickListener(object : CatInfoGalleryAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        val intent = Intent(context, PostActivity::class.java)
                        intent.putExtra("postKey", Uri.parse(postViewModel.getPosts()[position].key).toString()) // 해당 게시글로 갈 수 있도록 key 값을 넘겨서 화면 전환
                        startActivity(intent)
                    }
                })
            }
        })
    }

    // 글쓰기 버튼 리스너
    inner class ButtonListener : View.OnClickListener{
        override fun onClick(v: View?) {
            val intent = Intent(context, WritePost::class.java)
            startActivity(intent)
        }
    }
}