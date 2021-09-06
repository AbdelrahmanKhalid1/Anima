package com.ak.otaku_kun.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ak.otaku_kun.ui.search.results.character.CharacterFragment
import com.ak.otaku_kun.ui.search.results.staff.StaffFragment
import com.ak.otaku_kun.ui.search.results.studio.StudioFragment
import com.ak.otaku_kun.ui.search.results.user.UserFragment
import com.ak.otaku_kun.ui.search.results.media.MediaFragment
import com.ak.type.MediaType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val TAB_TITLES = arrayOf(
    "Anime",
    "Manga",
    "Characters",
    "Studio",
    "Staff",
    "User"
)

class SearchPagerAdapter(fa: FragmentActivity, private var query: String) : FragmentStateAdapter(fa) {

    fun connectTabWithPager(viewPager: ViewPager2, tabLayout: TabLayout){
        TabLayoutMediator(tabLayout, viewPager){tab, position->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    fun setQuery(query: String){
        this.query = query
    }

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> MediaFragment.newInstance(MediaType.ANIME, query)
            1 -> MediaFragment.newInstance(MediaType.MANGA, query)
            2 -> CharacterFragment.newInstance(query)
            3 -> StudioFragment.newInstance(query)
            4 -> StaffFragment.newInstance(query)
            else -> UserFragment.newInstance(query)
        }


    override fun getItemCount(): Int = TAB_TITLES.size
}