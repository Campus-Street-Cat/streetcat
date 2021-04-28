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
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class MainViewModel() : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()


    private var cats = ArrayList<Cat>()
    private var _cats = MutableLiveData<ArrayList<Cat>>()
    private var _catsRef = MutableLiveData<DatabaseReference>()


    fun getCats(): ArrayList<Cat>{
        return cats
    }

    fun addCat(img: Uri, name: String){
        cats.add(Cat(listcat[0], name))
        _cats.value = cats
        _catsRef.value = database.getReference("cats").child(name)
    }

    fun getCatRef(): DatabaseReference{
        return database.reference.child("cats")
    }

    fun getLiveRef(): MutableLiveData<DatabaseReference>{
        return _catsRef
    }
    private fun setImageUri(name: String, imageUri: String){
        database.getReference("cats").child(name).child("picture").setValue(imageUri)
    }

    fun setCatInfo(name: String, catClass: CatAddClass){
        database.getReference("cats").child(name).setValue(catClass)
    }

    fun setPhoto(uri: Uri, name: String){
        storage.reference.child(name).child("main").child(name + ".png").putFile(uri).addOnSuccessListener {
            val imageUri = it.uploadSessionUri.toString()
            setImageUri(name, imageUri)
        }
    }


    }


