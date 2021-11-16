package com.ak.anima.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ak.anima.ui.discover.DiscoverMediaFragment
import com.ak.anima.utils.Const
import com.ak.type.MediaType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DiscoverPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    fun connectTabWithPager(viewPager: ViewPager2, tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = Const.TRENDING_TAB_TITLES[position]
        }.attach()
    }

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> DiscoverMediaFragment.newInstance(MediaType.ANIME)
            else -> DiscoverMediaFragment.newInstance(MediaType.MANGA)
        }

    override fun getItemCount(): Int = Const.TRENDING_TAB_TITLES.size
}