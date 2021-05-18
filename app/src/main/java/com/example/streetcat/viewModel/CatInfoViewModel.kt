package com.example.streetcat.viewModel

import com.example.streetcat.data.Cat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.streetcat.activity.PostActivity
import com.example.streetcat.adapter.CatInfoGalleryAdapter
import com.example.streetcat.data.*
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_post.*
import java.net.URL

class CatInfoViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()



    fun getCatRef(key: String) : DatabaseReference {
        return database.getReference("cats").child(key)
    }
    fun getKey() : String{
        return "hi"
    }
}