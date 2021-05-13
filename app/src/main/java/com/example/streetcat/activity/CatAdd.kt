package com.example.streetcat.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.streetcat.R
import com.example.streetcat.data.CatAddClass
import com.example.streetcat.fragment.HomeFragment
import com.example.streetcat.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_cat_add.*


class CatAdd : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
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
            input_catPicture.setImageURI(uriPhoto)
        }
    }

    // 갤러리 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE),
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
            Log.d("TAG", "Permission: " + permissions[0] + "was " + grantResults[0])
        }else{
            Log.d("TAG", "Not Permission")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_add)

        //학교 이름 가져오기
        mainViewModel.getUserRef().child("schoolName").get().addOnSuccessListener {
            mainViewModel.setSchoolName(it.value.toString())
        }

        // 권한 체크
        input_catPicture.setOnClickListener{
            if(checkPersmission()){
                openGalleryForImage()
            }else{
                requestPermission()
            }
        }


        // "등록하기" 버튼을 누른 경우
        addCatBtn.setOnClickListener{

            val name = input_catName.editableText.toString()
            val birth = input_catBirth.editableText.toString()
            val neutral = input_neutral.isChecked
            val gender = input_male.isChecked

            mainViewModel.setCatRef()
            val key = mainViewModel.getKey()

            // Storage 고양이 사진 추가
            mainViewModel.setPhoto(uriPhoto!!, key)
            Log.d("tag", uriPhoto.toString())
            // DB 고양이 정보 추가
            val catClass = CatAddClass(name, birth, gender, neutral)


            mainViewModel.setCatInfo(key, catClass)
            Toast.makeText(applicationContext, "고양이가 등록되었습니다", Toast.LENGTH_SHORT).show()

            this.onBackPressed()
        }
    }
}


