package com.example.streetcat.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.streetcat.R
import com.example.streetcat.data.UserInfo
import com.example.streetcat.fragment.SettingFragment
import com.example.streetcat.viewModel.RegisterViewModel
import com.example.streetcat.viewModel.SettingViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cat_add.*
import kotlinx.android.synthetic.main.activity_change_picture_dialog.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.btn_profilePictureChange


class ProfileChangeActivity : AppCompatActivity() {
    private val SettingViewModel: SettingViewModel by viewModels()

    private var mAuth: FirebaseAuth? = null

    private var uriPhoto : Uri? = null

    // 카메라 권한 요청 및 권한 체크
    val REQUEST_GALLERY_TAKE = 2

    //갤러리 열기
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_TAKE)
    }

    // onActivityResult 로 이미지 설정
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_TAKE) {
            uriPhoto = data?.data

            // ImageButton 에 가져온 이미지 set
            input_catPicture2.setImageURI(uriPhoto)
        }
    }

    // 갤러리 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_GALLERY_TAKE)
    }

    // 갤러리 권한 체크
    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    // 권한요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }else{
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_picture_dialog)

        mAuth = FirebaseAuth.getInstance()

        input_catPicture2.setOnClickListener{
            if(checkPersmission()){
                openGalleryForImage()
            }else{
                requestPermission()
            }
        }


        btn_profilePictureChange.setOnClickListener()
        {
            val key = mAuth!!.currentUser.uid
            if(uriPhoto != null){
                SettingViewModel.setPhoto(uriPhoto!!, key)
            }
            onBackPressed()
        }

    }

}





