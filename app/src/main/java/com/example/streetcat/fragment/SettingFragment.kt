package com.example.streetcat.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.streetcat.R
import com.example.streetcat.activity.*
import com.example.streetcat.viewModel.RegisterViewModel
import com.example.streetcat.viewModel.SettingViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cat_add.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment : Fragment() {
    private val SettingViewModel: SettingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view: View = inflater!!.inflate(R.layout.fragment_setting, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btn_logout.setOnClickListener{ view ->
            Log.d("btnSetup", "Selected")
            val Auth = FirebaseAuth.getInstance()
            Auth.signOut()
            Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_changeProfile.setOnClickListener {
            val intent = Intent(context, ProfileChangeActivity::class.java)
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

