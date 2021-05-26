package com.example.streetcat.viewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import androidx.lifecycle.ViewModel

class CatInfoViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    private var userKey : String = ""

    fun getCatRef(key: String) : DatabaseReference {
        return database.getReference("cats").child(key)
    }

    fun getKey() : String{
        return "hi"
    }

    fun getUserRef(): DatabaseReference{
        userKey = mAuth!!.currentUser.uid
        return database.getReference("users").child(userKey)
    }

    fun addCat(key : String, name : String){
        database.getReference("users").child(userKey).child("cats").child(key).setValue(name)
    }

    fun deleteCat(key : String){
        database.getReference("users").child(userKey).child("cats").child(key).setValue(null)
    }
}