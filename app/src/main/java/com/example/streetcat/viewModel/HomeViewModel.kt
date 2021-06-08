package com.example.streetcat.viewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.activity.CatInfo
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.data.Cat
import com.example.streetcat.data.CatAddClass
import com.example.streetcat.fragment.HomeFragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList


class HomeViewModel() : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private var userKey : String = ""

    private var userSchool : String = ""
    private var cats = ArrayList<Cat>()
    private var schoolCats = ArrayList<String>()
    private var catKey : String = ""

    lateinit var schoolAdapter: HomeRecyclerViewAdapter //학교 고양이 리사이클러뷰 어댑터
    lateinit var favoriteAdapter: HomeRecyclerViewAdapter //즐겨찾기 고양이 리사이클러뷰 어댑터


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

    fun showHomeRcViews(context: Context, univ_cats_view: RecyclerView, favorite_cats_view: RecyclerView) {
        database.getReference("users").child(userKey).child("cats").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(data: DataSnapshot) {
                val userCat = ArrayList<Cat>()
                for(cat in data.children)
                    userCat.add(Cat(Uri.parse(""), cat.value.toString(), cat.key.toString()))

                // 고양이 참조 시작
                database.getReference("cats").addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val data = dataSnapshot.children
                        val schoolCat = ArrayList<Cat>()
                        for(cat in data){
                            val catSchool = cat.child("school").value.toString()
                            if(catSchool == getSchool()){
                                val img = Uri.parse(cat.child("picture").value.toString())
                                val name = cat.child("name").value.toString()
                                schoolCat.add(Cat(img, name, cat.key.toString()))
                            } // 학교 고양이 등록

                            for(usercat in userCat){
                                if(cat.key.toString() == usercat.catid){
                                    usercat.img = Uri.parse(cat.child("picture").value.toString())
                                }
                            }


                            // 학교 고양이 리사이클러뷰
                            univ_cats_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            schoolAdapter = HomeRecyclerViewAdapter(schoolCat)
                            univ_cats_view.adapter = schoolAdapter

                            schoolAdapter.setItemClickListener(object : HomeRecyclerViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val intent = Intent(context, CatInfo::class.java)
                                    intent.putExtra("catId", schoolCat[position].catid)
                                    intent.putExtra("catName", schoolCat[position].name)
                                    startActivity(context, intent, null)
                                }
                            })

                            // 즐겨찾는 고양이 리사이클러뷰
                            favorite_cats_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            favoriteAdapter = HomeRecyclerViewAdapter(userCat)
                            favorite_cats_view.adapter = favoriteAdapter

                            favoriteAdapter.setItemClickListener(object :
                                HomeRecyclerViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val intent = Intent(context, CatInfo::class.java)
                                    intent.putExtra("catId", userCat[position].catid)
                                    intent.putExtra("catName", userCat[position].name)
                                    startActivity(context, intent, null)
                                }
                            })

                        }
                    } // onDataChange
                }) // AllCatRef
            } // onDataChange
        }) // UserRef
    }

    fun showRandomCat(context: Context) {
        database.getReference("cats").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val num = p0.childrenCount.toInt()
                val random = Random()
                val randNum = random.nextInt(num)

                var cnt = 0

                for (data in p0.children) {
                    if (cnt == randNum) {
                        val cat = data.key.toString()
                        database.getReference("cats").child(cat).get().addOnSuccessListener {
                            val intent = Intent(context, CatInfo::class.java)
                            intent.putExtra("catId", it.key.toString())
                            intent.putExtra("catName", it.child("name").value.toString())
                            startActivity(context, intent, null)
                        }
                    }

                    cnt++
                }
            }
        })
    }
}


