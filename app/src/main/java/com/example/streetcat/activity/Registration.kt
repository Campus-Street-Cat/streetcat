package com.example.streetcat.activity

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.streetcat.R
import com.example.streetcat.data.UserInfo
import com.example.streetcat.viewModel.PostViewModel
import com.example.streetcat.viewModel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*


class Registration : AppCompatActivity() {
    private val RegisterViewModel: RegisterViewModel by viewModels()
    private val TAG = "FirebaseEmailPassword"
    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth = FirebaseAuth.getInstance()

        btn_registration.setOnClickListener()
        {
            val email = register_input_email.text.toString()
            val password = register_input_password.text.toString()
            val nickname = register_input_nickname.text.toString()
            val school = register_input_school.text.toString()
            Log.e(TAG, "createAccount:" + email)
            if (!validateForm(email, password, nickname, school)) {
                return@setOnClickListener
            }

            //호출 부분
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        RegisterViewModel.setUserRef()
                        val key = RegisterViewModel.getKey()

                        val user = UserInfo(email, password, school, nickname)
                        RegisterViewModel.setInfo(key, user)
                        startActivity(Intent(this@Registration, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
                    }
                }


        }

    }

    private fun validateForm(email: String, password: String, nickName: String, schoolName: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(nickName)) {
            Toast.makeText(applicationContext, "Enter nickName!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(schoolName)) {
            Toast.makeText(applicationContext, "Enter schoolName!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}





