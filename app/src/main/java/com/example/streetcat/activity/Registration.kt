package com.example.streetcat.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*


class Registration : AppCompatActivity() {
    //
    lateinit var auth:FirebaseAuth //파이어베이스 선언
    var databaseReference: DatabaseReference? = null //실시간 데베 참조
    var database:FirebaseDatabase? = null //자체 데베 참조


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_info)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile") //테이블 이름 지정
        register() //레지스터 함수 호출

    }

    private fun register() {
        btn_registration.setOnClickListener()
        {
            val name = input_name.toString()
            val email = input_email.toString()
            val password = input_password.toString()

            if (TextUtils.isEmpty(name)) {
                input_name.setError("이름을 입력해주세요")
                //비워둔 상태 허용불가
                return@setOnClickListener
            } else if (TextUtils.isEmpty(email)) {
                input_name.setError("이메일을 입력해주세요")
                //비워둔 상태 허용불가
                return@setOnClickListener
            } else if (TextUtils.isEmpty(password)) {
                input_name.setError("비밀번호를 입력해주세요")
                //비워둔 상태 허용불가
                return@setOnClickListener
            }

            //호출 부분
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { //확인
                    if (it.isSuccessful) //성공했을 경우
                    {
                        val currentUser = auth.currentUser
                        val currentUserDb = databaseReference?.child(currentUser?.uid!!) //성공시 저장해줌
                        currentUserDb?.child(name)?.setValue(input_name.text.toString())
                        Toast.makeText(this@Registration, "등록 성공하였습니다.", Toast.LENGTH_SHORT).show()
                        finish() //액티비티 끝냄
                    } else //실패경우
                    {
                        Toast.makeText(this@Registration, "등록 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            //이메일과 패스워드로 사용자 지정, 전달함
        }
    }
}





