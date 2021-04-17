package com.example.streetcat

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_cat_add.*
import kotlinx.android.synthetic.main.item_recycler_view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CatAdd : AppCompatActivity() {

    var uriPhoto : Uri? = null

    // 카메라 권한 요청 및 권한 체크
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY_TAKE = 2
    lateinit var currentPhotoPath : String

    //갤러리 열기
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_TAKE)
    }


    // onActivityResult 로 이미지 설정
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            // 갤러리를 통해 이미지 가져오는 경우
            2 -> {
                if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_TAKE) {
                    uriPhoto = data?.data

                    // ImageButton 에 가져온 이미지 set
                    input_catPicture.setImageURI(uriPhoto)
                }
            }
        }
    }
    // 카메라 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
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

    // 카메라 열기
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                // 찍은 사진을 그림파일로 만들기
                val photoFile: File? =
                        try {
                            createImageFile()
                        } catch (ex: IOException) {
                            Log.d("TAG", "그림파일 만드는도중 에러생김")
                            null
                        }

                // 그림파일을 성공적으로 만들었다면 onActivityForResult로 보내기
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this, "com.example.streetcat.fileprovider", it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }


    // 카메라로 촬영한 이미지를 파일로 저장해준다
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            1 -> {
                if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){

                    // 카메라로부터 받은 데이터가 있을경우에만
                    val file = File(currentPhotoPath)
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media
                                .getBitmap(contentResolver, Uri.fromFile(file))  //Deprecated
                        input_catPicture.setImageBitmap(bitmap)
                    }
                    else{
                        val decode = ImageDecoder.createSource(this.contentResolver,
                                Uri.fromFile(file))
                        val bitmap = ImageDecoder.decodeBitmap(decode)
                        input_catPicture.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_add)


        // database 생성
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("cats")
        var gender = "male"
        var neutral = false


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
            val school = input_catSchool.editableText.toString()
            var imgFileName = name + ".png"

            var fbStorage = FirebaseStorage.getInstance()

            var storageRef = fbStorage.reference.child(name).child("main").child(imgFileName)
            storageRef.putFile(uriPhoto!!).addOnSuccessListener {
                var img_url = it.uploadSessionUri.toString()

                ref.child(name).child("picture").setValue(img_url)
            }
            if(input_male.isChecked) {
                gender = "male"
            }
            else{
                gender = "female"
            }
            neutral = input_neutral.isChecked

            val info_cat = CatAddClass(name, birth, gender, neutral, school)
            ref.child(name).setValue(info_cat)
            Toast.makeText(applicationContext, "고양이가 등록되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}


