package com.example.streetcat.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.streetcat.R
import com.example.streetcat.activity.CatAdd
import com.example.streetcat.activity.LoginActivity
import com.example.streetcat.activity.NoticeActivity
import com.example.streetcat.activity.SchoolAuth
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view: View = inflater!!.inflate(R.layout.fragment_setting, container, false)
        view.btn_school_auth.setOnClickListener { view ->
            Log.d("btn_school", "Selected")
            val intent = Intent(context, SchoolAuth::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_notice.setOnClickListener { view ->
            val intent = Intent(context, NoticeActivity::class.java)
            startActivity(intent)
        }

        btn_logout.setOnClickListener{ view ->
            Log.d("btnSetup", "Selected")
            val Auth = FirebaseAuth.getInstance()
            Auth.signOut()
            Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)

        }


/*
        super.onViewCreated(view, savedInstanceState)
        btn_school_auth.setOnClickListener{view ->
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }*/
    }

    //로그아웃 리스너
    inner class ButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {

        }
    }



}

