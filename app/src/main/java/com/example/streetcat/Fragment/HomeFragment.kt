package com.example.streetcat.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.example.streetcat.CatActivity
import com.example.streetcat.CatInfo
import com.example.streetcat.MainActivity
import com.example.streetcat.R
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*pom.setOnClickListener()
        {
            val intent = Intent(activity, CatInfo::class.java); //화면이동
            startActivity(intent);
        }

        btn_add.setOnClickListener()
        {
            val intent = Intent(activity, CatActivity::class.java); //화면이동
            startActivity(intent);
        }
*/
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


}