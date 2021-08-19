package com.ak.otaku_kun.ui.adapter.paging

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ak.otaku_kun.R
import com.ak.otaku_kun.ui.discover.DiscoverMediaFragment
import com.ak.type.MediaType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val TAB_TITLES = arrayOf(
    "Anime",
    "Manga"
)
class DiscoverPagingAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    fun connectTabWithPager(viewPager: ViewPager2, tabLayout: TabLayout){
        TabLayoutMediator(tabLayout, viewPager){tab, position->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    override fun createFragment(position: Int): Fragment =
        when(position) {
            0 -> DiscoverMediaFragment.newInstance(MediaType.ANIME)
            else -> DiscoverMediaFragment.newInstance(MediaType.MANGA)
        }
    override fun getItemCount(): Int = TAB_TITLES.size
}