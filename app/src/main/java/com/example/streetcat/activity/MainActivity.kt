package com.example.streetcat.activity

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R
import com.example.streetcat.adapter.HomeViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private fun configureBottomNavigation(){


        vp_ac_main_frag_pager.adapter = HomeViewPagerAdapter(supportFragmentManager, 4)

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

        //현재 기기의 토큰을 가져와서 출력 해보자.
        /*cat_health.setOnClickListener {
            startActivity(
                Intent(this@MainActivity,
                SickSelect::class.java)
            ) //등록링크에서 등록시작
        }*/
    }


}