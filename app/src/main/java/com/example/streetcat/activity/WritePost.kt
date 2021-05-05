package com.example.streetcat.activity

import android.Manifest
import android.app.Activity
import android.content.ClipData
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
import com.example.streetcat.data.PostClass
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cat_add.input_catPicture
import kotlinx.android.synthetic.main.activity_write_post.*

class WritePost : AppCompatActivity() {
    private val postViewModel: PostViewModel by viewModels()
    //private var uriPhoto : Uri? = null
    private var uriPhoto = ArrayList<Uri?>()

    // 카메라 권한 요청 및 권한 체크
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY_TAKE = 2
    //lateinit var currentPhotoPath : String

    //갤러리 열기
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 사진 여러 장 가져오는 코드?
        startActivityForResult(intent, REQUEST_GALLERY_TAKE)
        //startActivityForResult(intent, MULTI)
    }


    // onActivityResult 로 이미지 설정
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            // 갤러리를 통해 이미지 가져오는 경우
            2 -> {
                if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_TAKE) {
                    val clipdata : ClipData? = data?.clipData
                    if (clipdata != null) {
                        for(i in 0 until clipdata.itemCount){
                            uriPhoto.add(clipdata.getItemAt(i).uri)
                        } // 사진 여러장 저장하기
                    }

                    //uriPhoto = data?.data
                    //uriPhoto.add(data?.data)

                    // ImageButton 에 가져온 이미지 set
                    input_catPicture.setImageURI(uriPhoto[0])
                    //input_catPicture.setImageURI(uriPhoto)
                }
            }
        }
    }
    // 카메라 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                REQUEST_IMAGE_CAPTURE)
    }

    // 카메라 권한 체크
    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
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

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)

        input_catPicture.setOnClickListener{
            if(checkPersmission()){
                openGalleryForImage()
            }else{
                requestPermission()
            }
        }

        writePost_button.setOnClickListener{
            //Log.d("clipdata", uriPhoto.toString())
            val contents = postContents.editableText.toString()
            val username : String = "pompom_love" // 글쓴 유저 이름 로그인 정보에서 가져와야 할듯

            postViewModel.setPostRef()
            val key = postViewModel.getKey()

            postViewModel.setPhoto(uriPhoto!!, key)
            postViewModel.addPost(uriPhoto!!, key)

            val post = PostClass(username, 0, 0, contents, uriPhoto.size)
            postViewModel.setPost(key, post)
            Toast.makeText(applicationContext, "게시글이 등록되었습니다", Toast.LENGTH_SHORT).show()
            this.onBackPressed()
        }
    }
}