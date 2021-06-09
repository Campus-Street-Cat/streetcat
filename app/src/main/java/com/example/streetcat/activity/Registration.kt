package com.example.streetcat.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.streetcat.R
import com.example.streetcat.data.UserInfo
import com.example.streetcat.viewModel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.android.synthetic.main.activity_cat_add.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*


class Registration : AppCompatActivity() {
    private val RegisterViewModel: RegisterViewModel by viewModels()
    private val TAG = "FirebaseEmailPassword"
    private var mAuth: FirebaseAuth? = null
    private val schools = arrayListOf("-학교 선택-", "한국항공대학교", "서울대학교", "KAIST")

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
            btn_addPicture.setHint(data?.dataString)
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
        setContentView(R.layout.activity_registration)
        val schoolAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, schools)
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        register_input_school.adapter = schoolAdapter

        register_input_school.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val a = 1 // 아무것도 안쓰면 에러가 나서 아무거나 썼음..
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        mAuth = FirebaseAuth.getInstance()

        btn_addPicture.setOnClickListener{
            if(checkPersmission()){
                openGalleryForImage()
            }else{
                requestPermission()
            }
        }


        btn_profilePictureChange.setOnClickListener()
        {
            val email = register_input_email.text.toString()
            val password = register_input_password.text.toString()
            val nickname = register_input_nickname.text.toString()
            val school = register_input_school.selectedItem.toString()
            if (!validateForm(email, password, nickname, school)) {
                return@setOnClickListener
            }



            //호출 부분
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        RegisterViewModel.setUserRef()
                        val key = mAuth!!.currentUser.uid
                        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                            FirebaseService.token = it.token
                            RegisterViewModel.setToken(it.token, key)
                        }
                        if(uriPhoto != null){
                            RegisterViewModel.setPhoto(uriPhoto!!, key)
                        }
                        val user = UserInfo(email, school, nickname)
                        RegisterViewModel.setInfo(key, user)
                        startActivity(Intent(this@Registration, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "잘못된 인증 시도입니다.", Toast.LENGTH_SHORT).show()
                    }
                }


        }

    }

    private fun validateForm(email: String, password: String, nickName: String, schoolName: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "이메일 주소를 입력하세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(nickName)) {
            Toast.makeText(applicationContext, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(schoolName)) {
            Toast.makeText(applicationContext, "학교 이름을 입력하세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "비밀번호는 6자리 이상이어야합니다", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}