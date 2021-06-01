package com.example.streetcat.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.streetcat.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class SettingViewModel() : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private var userKey : String = ""

    fun setPassword(password : String){
        Firebase.auth.currentUser?.updatePassword(password)
    }

    fun setNickname(nickName: String){
        database.getReference("users").child(userKey).child("nickName").setValue(nickName)
    }
    fun setUserRef(){ // 한 포스트에 대해서 push 함수로 키 만들어두고 그 키 값을 저장해둠
        userKey = database.getReference("users").push().key.toString()
    }

    fun getKey() : String{
        return userKey
    }

    fun getUserRef(): DatabaseReference{
        userKey = mAuth!!.currentUser.uid
        return database.getReference("users").child(userKey)
    }

    fun setInfo(key: String, post: UserInfo){
        database.getReference("users").child(key).setValue(post)
    }

    private fun setImageUri(name: String, imageUri: String){
        database.getReference("users").child(name).child("picture").setValue(imageUri)
    }

    fun setPhoto(uri: Uri, name: String){
        val storageRef = storage.reference.child(name).child("main").child(name + ".png")
        storageRef?.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                setImageUri(name, uri.toString())
            }
        }
    }
}