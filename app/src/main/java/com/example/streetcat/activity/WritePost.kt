package com.example.streetcat.activity

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.R
import com.example.streetcat.adapter.CheckboxAdapter
import com.example.streetcat.data.PostClass
import com.example.streetcat.viewModel.PostViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cat_add.input_catPicture
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_write_post.*

class WritePost : AppCompatActivity() {
    private val postViewModel: PostViewModel by viewModels()
    private var uriPhoto = ArrayList<Uri?>()
    //private val schools = arrayListOf("- 학교 선택 -", "한국항공대학교", "서울대학교", "KAIST")
    private val schools = ArrayList<String>()
    private val schoolCats = ArrayList<String>() // 고양이 키 값 저장

    private var selectedSchool : String = ""
    lateinit var checkboxAdapter: CheckboxAdapter

    // 카메라 권한 요청 및 권한 체크
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY_TAKE = 2

    //갤러리 열기
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 사진 여러 장 가져오는 코드?
        startActivityForResult(intent, REQUEST_GALLERY_TAKE)
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
                    // ImageButton 에 가져온 이미지 set
                    input_catPicture.setImageURI(uriPhoto[0])
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
        schools.add(" -학교 선택 -")

        postViewModel.setCatRef()
        val Ckey = postViewModel.getCatKey()

        // 유저 닉네임 set
        postViewModel.getUserRef().child("nickName").get().addOnSuccessListener {
            postViewModel.setNickname(it.value.toString())
        }

        postViewModel.getSchoolRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(data in dataSnapshot.children){
                    schools.add(data.key.toString())
                }
            }
        })

        // 학교 spinner
        val schoolAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, schools)
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        school.adapter = schoolAdapter

        val cont = this

        school.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedSchool = school.selectedItem.toString()
                postViewModel.getSchoolRef().addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(schoolCats.isNotEmpty())
                            schoolCats.clear()
                        for(data in dataSnapshot.children){
                            if(data.key == selectedSchool){
                                val cats = data.child("cats").children
                                for(cat in cats){
                                    schoolCats.add(cat.key.toString())
                                }

                                school_cat.layoutManager = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                                checkboxAdapter = CheckboxAdapter(schoolCats, postViewModel, Ckey)
                                school_cat.adapter = checkboxAdapter
                            }
                        }
                    }
                })
            } // onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        input_catPicture.setOnClickListener{
            if(checkPersmission()){
                openGalleryForImage()
            }else{
                requestPermission()
            }
        }

        writePost_button.setOnClickListener{
            val contents = postContents.editableText.toString()
            val username = postViewModel.getNickname()

            postViewModel.setPostRef()
            val key = postViewModel.getKey()

            postViewModel.setPhoto(uriPhoto!!, key)
            postViewModel.addPost(uriPhoto!!, key)

            val post = PostClass(username, contents, uriPhoto.size)
            postViewModel.setPost(key, post)
            postViewModel.setSchool(key, selectedSchool)


            val selectedCats = ArrayList<String>()
            postViewModel.getCatRef().addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(data in dataSnapshot.children){
                        if(data.key == Ckey){
                            val temp = data.child("cats").children

                            for(cat in temp){
                                selectedCats.add(cat.key.toString())
                            }
                            postViewModel.addPostCats(key, selectedCats)
                            postViewModel.deleteCats(Ckey)
                        }
                    }
                }
            })



            Toast.makeText(applicationContext, "게시글이 등록되었습니다", Toast.LENGTH_SHORT).show()
            this.onBackPressed()
        }
    }
}