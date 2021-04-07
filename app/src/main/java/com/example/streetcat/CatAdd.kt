package com.example.streetcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cat_add.*

class CatAdd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_add)

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("cats")


        addCatBtn.setOnClickListener{

            val name = input_catName.editableText.toString()
            val birth = input_catBirth.editableText.toString()
            val sex = "male"
            val neutral = false
            val info_cat = CatAddClass(name, birth, sex, neutral)
            ref.setValue(info_cat)
        }
    }
}