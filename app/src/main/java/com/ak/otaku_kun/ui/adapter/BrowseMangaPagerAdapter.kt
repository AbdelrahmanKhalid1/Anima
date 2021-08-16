package com.ak.otaku_kun.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ak.otaku_kun.R
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.type.MediaSeason
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val TAB_TITLES = arrayOf(
    R.string.winter,
    R.string.spring,
    R.string.summer,
    R.string.fall
)/*
class BrowseMangaPagerAdapter (private val context: Context, fa: FragmentActivity, private val queryFilters: QueryFilters) : FragmentStateAdapter(fa) {

    fun connectTabWitPager(viewPager: ViewPager2, tabLayout: TabLayout){
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = context.getString(TAB_TITLES[position])
        }.attach()
    }
    override fun getItemCount(): Int = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BrowseAnimeFragment.newInstance( queryFilters)
            1 -> BrowseAnimeFragment.newInstance( queryFilters)
            2 -> BrowseAnimeFragment.newInstance( queryFilters)
            3 -> BrowseAnimeFragment.newInstance( queryFilters)
            else -> BrowseAnimeFragment.newInstance( queryFilters)
        }
    }

}*/