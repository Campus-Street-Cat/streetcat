package com.example.streetcat.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class FbViewModel() : ViewModel() {
    private lateinit var databaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference

    fun setDatabase(instance : DatabaseReference){
        databaseRef = instance
    }

    fun setStorage(instance : StorageReference){
        storageRef = instance
    }
}