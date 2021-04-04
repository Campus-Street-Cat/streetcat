package com.example.streetcat.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.streetcat.Fragment.*

class ViewPagerAdapter(fm : FragmentManager, private val fragmentCount : Int) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment()
            1 -> return NoticeFragment()
            2 -> return PostFragment()
            else -> return SettingFragment()
        }
    }

    override fun getCount(): Int = fragmentCount // 자바에서는 { return fragmentCount }

}