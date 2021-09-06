package com.ak.otaku_kun.ui.base.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ak.otaku_kun.utils.Const
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

abstract class BasePagerAdapter(fa :FragmentActivity) : FragmentStateAdapter(fa) {
    fun connectTabWithPager(viewPager: ViewPager2, tabLayout: TabLayout){
        TabLayoutMediator(tabLayout, viewPager){tab, position->
            tab.text = Const.TRENDING_TAB_TITLES[position]
        }.attach()
    }
}