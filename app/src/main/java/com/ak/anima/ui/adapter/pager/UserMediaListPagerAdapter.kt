package com.ak.anima.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ak.anima.ui.user_list.UserMediaListFragment
import com.ak.anima.utils.Const
import com.ak.type.MediaListStatus
import com.ak.type.MediaType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserMediaListPagerAdapter(fa: FragmentActivity, private val mediaType: MediaType) :
    FragmentStateAdapter(fa) {

    init {
        Const.USER_LIST_MEDIA_TAB_TITLES[0] =
            if (mediaType == MediaType.MANGA) "Reading" else "Watching"
    }

    fun connectTabWithPager(viewPager: ViewPager2, tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = Const.USER_LIST_MEDIA_TAB_TITLES[position]
        }.attach()
    }

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> UserMediaListFragment.newInstance(mediaType, MediaListStatus.CURRENT)
            1 -> UserMediaListFragment.newInstance(mediaType,MediaListStatus.REPEATING)
            2 -> UserMediaListFragment.newInstance(mediaType,MediaListStatus.PLANNING)
            3 -> UserMediaListFragment.newInstance(mediaType,MediaListStatus.COMPLETED)
            4 -> UserMediaListFragment.newInstance(mediaType,MediaListStatus.PAUSED)
            5 -> UserMediaListFragment.newInstance(mediaType,MediaListStatus.DROPPED)
            else -> UserMediaListFragment.newInstance(mediaType, MediaListStatus.UNKNOWN__)
        }
    override fun getItemCount(): Int = Const.USER_LIST_MEDIA_TAB_TITLES.size
}