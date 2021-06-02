package com.example.streetcat.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cat_add.*
import kotlinx.android.synthetic.main.activity_change_password_dialog.*
import kotlinx.android.synthetic.main.activity_change_password_dialog.view.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import kotlinx.android.synthetic.main.item_recycler_view.view.*

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

        btn_changePassword.setOnClickListener {
            showChangePasswordPopup()
        }

        btn_changeNickName.setOnClickListener {
            showChangeNickNamePopup()
        }

        btn_notice.setOnClickListener {
            val intent = Intent(context, NoticeActivity::class.java)
            startActivity(intent)
        }

        SettingViewModel.getUserRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(data: DataSnapshot) {
                //input_userNickname.text = data.child("nickName").value.toString()
                input_userSchool.text = data.child("schoolName").value.toString()
                Picasso.get().load(Uri.parse(data.child("picture").value.toString())).error(R.drawable.common_google_signin_btn_icon_dark).into(input_userPicture)
            }
        })

        SettingViewModel.getUserRef().child("nickName").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(data: DataSnapshot) {
                input_userNickname.text = data.value.toString()
            }
        })

    }

    private fun showChangePasswordPopup(){
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.activity_change_password_dialog, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("비밀번호 변경")

        val mAlertDialog = mBuilder.show()

        val okButton = mDialogView.findViewById<Button>(R.id.btn_change_password)
        val textPass = mDialogView.findViewById<EditText>(R.id.text_change_password)
        okButton.setOnClickListener {
            Toast.makeText(context, "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show()
            SettingViewModel.setPassword(textPass.text.toString())
            mAlertDialog.dismiss()
        }
    }

    private fun showChangeNickNamePopup(){
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.activity_change_password_dialog, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("닉네임 변경")

        val mAlertDialog = mBuilder.show()
        mDialogView.text_change_password.setHint("닉네임")
        val okButton = mDialogView.findViewById<Button>(R.id.btn_change_password)
        val textPass = mDialogView.findViewById<EditText>(R.id.text_change_password)
        okButton.setOnClickListener {
            Toast.makeText(context, "닉네임이 변경되었습니다", Toast.LENGTH_SHORT).show()
            SettingViewModel.setNickname(textPass.text.toString())
            mAlertDialog.dismiss()
        }
    }

}

