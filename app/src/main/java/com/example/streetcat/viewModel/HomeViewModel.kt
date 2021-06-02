package com.example.streetcat.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.streetcat.data.Cat
import com.example.streetcat.data.CatAddClass
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class HomeViewModel() : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private var userKey : String = ""

    private var userSchool : String = ""
    private var cats = ArrayList<Cat>()
    private var schoolCats = ArrayList<String>()
    private var catKey : String = ""


    fun getCats(): ArrayList<Cat>{
        return cats
    }

    fun setCatSick(catId: String, sickName: String){
        database.getReference("cats").child(catId).child("sick").setValue(sickName)
    }

    fun getSchool(): String {
        return userSchool
    }
    fun setSchoolName(schoolName: String){
        userSchool = schoolName
    }
    fun getUserRef(): DatabaseReference{
        userKey = mAuth!!.currentUser.uid
        return database.getReference("users").child(userKey)
    }
    fun addCat(img: Uri, name: String, catid: String){
        cats.add(Cat(img, name, catid))
    }

    fun getSchoolCatsRef(): DatabaseReference{
        return database.getReference("cats").child(userSchool).child("cats")
    }
    fun addSchoolCats(catStr: String){
        schoolCats.add(catStr)
    }
    //자기 학교의 고양이 리스트 참조
    fun getCatRef(): DatabaseReference{
        return database.getReference("schools").child(userSchool).child("cats")
    }

    fun getAllCatRef(): DatabaseReference{
        return database.getReference("cats")
    }

    fun setCatRef(){ // 한 포스트에 대해서 push 함수로 키 만들어두고 그 키 값을 저장해둠
        catKey = database.getReference("cats").push().key.toString()
    }

    fun getKey() : String{
        return catKey
    }

    private fun setImageUri(name: String, imageUri: String){
        database.getReference("cats").child(name).child("picture").setValue(imageUri)
    }

    fun setCatInfo(name: String, catClass: CatAddClass){
        database.getReference("cats").child(name).setValue(catClass)
        database.getReference("cats").child(name).child("school").setValue(userSchool)
        database.getReference("schools").child(userSchool).child("cats").child(catClass.name).setValue(name)
    }

    fun setPhoto(uri: Uri, name: String){
        val storageRef = storage.reference.child(name).child("main").child(name + ".png")
        storageRef?.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                setImageUri(name, uri.toString())
            }
        }
    }
    fun getCatRef(key: String) : DatabaseReference{
        return database.getReference("cats").child(key)
    }
}


