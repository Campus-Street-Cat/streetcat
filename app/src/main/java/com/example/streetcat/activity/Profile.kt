package com.example.streetcat.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login_info.*
import kotlinx.android.synthetic.main.activity_profile.*



class Profile : AppCompatActivity() {

    lateinit var auth: FirebaseAuth //파이어베이스 선언
    var databaseReference: DatabaseReference? = null //실시간 데베 참조
    var database: FirebaseDatabase? = null //자체 데베 참조

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_info)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile") //테이블 이름 지정
        loadProfile()
    }

    private fun loadProfile()
    {
        val user = auth.currentUser //현재 사용자가 로그인한 사용자로
        val userreference = databaseReference?.child(user?.uid!!)

        tv_emailinfo.text = "이메일은" + user?.email //유저의 이메일을 반환

        userreference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                tv_nameinfo.text = "이름은" + snapshot.child("name").value.toString() //이름값 전달
            }
            override fun onCancelled(error:DatabaseError){

            }

    })
        btn_logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@Profile, Login::class.java))
            finish()
        }
    }
}






