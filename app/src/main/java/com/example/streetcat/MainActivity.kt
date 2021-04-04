package com.example.streetcat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.Adapter.ViewPagerAdapter
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private fun configureBottomNavigation(){


        vp_ac_main_frag_pager.adapter = ViewPagerAdapter(supportFragmentManager, 4)

        tl_ac_main_bottom_menu.setupWithViewPager(vp_ac_main_frag_pager)

        val bottomNaviLayout: View = this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)

        tl_ac_main_bottom_menu.getTabAt(0)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_home_tab) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(1)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_search_tab) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(2)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_add_tab) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(3)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_like_tab) as RelativeLayout

    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureBottomNavigation()


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")

        /*
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
        */



    }


}