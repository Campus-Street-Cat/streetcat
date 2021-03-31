package com.example.streetcat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val database = Firebase.database
        val myRef = database.getReference("nickname")

        myRef.setValue("Hello, World!")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pom: ImageButton = findViewById(R.id.pom);
        pom.setOnClickListener()
        {

            val intent = Intent(this, CatInfo::class.java); //화면이동
            startActivity(intent);
        }

        val btn_add: ImageButton = findViewById(R.id.btn_add);
        btn_add.setOnClickListener()
        {
            Toast.makeText(this@MainActivity, "고양이를 추가합니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CatActivity::class.java); //화면이동
            startActivity(intent);
        }



    }
}