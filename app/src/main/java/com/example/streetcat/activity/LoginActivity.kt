package com.example.streetcat.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R

import android.view.View
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.util.Log
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_cat_main.*

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "FirebaseEmailPassword"

    private var Auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_email_sign_in.setOnClickListener(this)
        btn_email_create_account.setOnClickListener(this)


        Auth = FirebaseAuth.getInstance()

        val currentUser = Auth!!.currentUser
        if(currentUser != null)
        {
            startActivity(Intent(this@LoginActivity, //로그인됨
                MainActivity::class.java))
            finish()
        }

    }

    override fun onStart() {
        super.onStart()

        val currentUser = Auth!!.currentUser
    }

    override fun onClick(view: View?) {
        val i = view!!.id

        if (i == R.id.btn_email_create_account) {
            startActivity(Intent(this@LoginActivity, Registration::class.java))
        } else if (i == R.id.btn_email_sign_in) {
            signIn(edtEmail.text.toString(), edtPassword.text.toString())
        }
    }


    private fun signIn(email: String, password: String) {
        Log.e(TAG, "signIn:" + email)
        if (!validateForm(email, password)) {
            return
        }

        Auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "signIn: Success!")

                    // update UI with the signed-in user's information
                    val user = Auth!!.getCurrentUser()

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Log.e(TAG, "signIn: Fail!", task.exception)
                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun signOut() {
        Auth!!.signOut()
    }



    private fun validateForm(email: String, password: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }



}
