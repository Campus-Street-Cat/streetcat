package com.example.streetcat.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.streetcat.data.*
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.net.URL


class RegisterViewModel() : ViewModel() {

    private val database = FirebaseDatabase.getInstance()

    private var userKey : String = ""

    
    fun setUserRef(){ // 한 포스트에 대해서 push 함수로 키 만들어두고 그 키 값을 저장해둠
        userKey = database.getReference("users").push().key.toString()
    }

    fun getKey() : String{
        return userKey
    }

    fun setInfo(key: String, post: UserInfo){
        database.getReference("users").child(key).setValue(post)
    }
}
