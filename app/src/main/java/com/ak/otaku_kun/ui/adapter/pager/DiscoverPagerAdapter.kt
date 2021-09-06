package com.ak.otaku_kun.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ak.otaku_kun.ui.base.adapter.BasePagerAdapter
import com.ak.otaku_kun.ui.discover.DiscoverMediaFragment
import com.ak.otaku_kun.utils.Const
import com.ak.type.MediaType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DiscoverPagerAdapter(fa: FragmentActivity) : BasePagerAdapter(fa) {
    override fun createFragment(position: Int): Fragment =
        when(position) {
            0 -> DiscoverMediaFragment.newInstance(MediaType.ANIME)
            else -> DiscoverMediaFragment.newInstance(MediaType.MANGA)
        }
    override fun getItemCount(): Int = Const.TRENDING_TAB_TITLES.size
}
