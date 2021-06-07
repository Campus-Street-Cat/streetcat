package com.example.streetcat.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.R
import com.example.streetcat.activity.*
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.adapter.NoticeRecyclerViewAdapter
import com.example.streetcat.data.Cat
import com.example.streetcat.data.Notice
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notice.*
import kotlinx.android.synthetic.main.fragment_notice.view.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*

class NoticeFragment : Fragment() {
    private val postViewModel: PostViewModel by viewModels()
    lateinit var noticeAdapter: NoticeRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view: View = inflater!!.inflate(R.layout.fragment_notice, container, false)

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postViewModel.getUserRef().child("notice").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(data: DataSnapshot) {

                val noticeList = ArrayList<Notice>()
                for (notice in data.children) {
                    val type = notice.child("type").value.toString()
                    val contexts = notice.child("context").value.toString()
                    val postkey = notice.child("postkey").value.toString()
                    val username = notice.child("username").value.toString()
                    noticeList.add(Notice(type, contexts, postkey, username))
                }

                rc_notice_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                noticeAdapter = NoticeRecyclerViewAdapter(noticeList)
                rc_notice_view.adapter = noticeAdapter

                noticeAdapter.setItemClickListener(object : NoticeRecyclerViewAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        Log.d("클릭 로그", "click")
                        val intent = Intent(context, PostActivity::class.java)
                        intent.putExtra("postKey", noticeList[position].postKey)
                        intent.putExtra("username", postViewModel.getUserKey())
                        startActivity(intent)
                    }
                })


            }
        })

        btn_fcm_test.setOnClickListener {
            val intent = Intent(context, FcmActivity::class.java)
            startActivity(intent)
        }
    }


}