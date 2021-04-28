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
        cats.add(Cat(img, name))
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
        val storageRef = storage.reference.child(name).child("main").child(name + ".png")
        storageRef?.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                setImageUri(name, uri.toString())
            }
        }
    }

    fun getPhoto(key: String) : Uri{
        var tmpUri : Uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/streetcat-fd0b0.appspot.com/o/cats%2FKakaoTalk_20210310_215259084_15.jpg?alt=media&token=4a4a8012-3d95-4c2f-8aeb-32a826d6599f")
        Log.d("error", key)
        val ref = storage.reference.child("caticon.PNG")
        ref.downloadUrl.addOnSuccessListener {
            tmpUri = it
            Log.d("error", "error1")
        }.addOnFailureListener{
            Log.d("error", "error2")
        }
        return tmpUri
    }

    }


