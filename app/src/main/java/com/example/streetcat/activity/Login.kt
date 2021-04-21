package com.example.streetcat.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_info.*


class Login : AppCompatActivity() {
    //
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_info)

        auth = FirebaseAuth.getInstance() //oncreate에서 초기화할때 필요한 사항

        val currentUser = auth.currentUser
        if(currentUser != null)
        {
            startActivity(Intent(this@Login, //로그인됨
                Profile::class.java))
            finish()
        }
        login()

    }

    private fun login() {
        btn_login.setOnClickListener {
            if (TextUtils.isEmpty(input_name.text.toString())) {
                input_name.setError("잘못된 이름입니다.")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(input_password.text.toString())) {
                input_password.setError("잘못된 비밀번호입니다.")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(
                input_name.text.toString(),
                input_password.text.toString()
            ) //리턴해줌
                .addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        startActivity(Intent(this@Login,
                            Profile::class.java))
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this@Login, "등록 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        tv_registration.setOnClickListener {
            startActivity(Intent(this@Login,
                Registration::class.java)) //등록링크에서 등록시작
        }
    }
}




