package com.ak.anima.ui.adapter.pager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ak.anima.R
import com.ak.anima.ui.details.media.fragment.*

class MediaPagerAdapter(
    fm: FragmentManager,
    private val context: Context,
    private val mediaId: Int
) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = titles.size

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> MediaOverviewFragment()
            1 -> MediaRelationFragment()
            2-> MediaCharacterFragment()
//            3 -> MediaEpisodeFragment()
            3-> MediaStaffFragment()
            else -> MediaReviewsFragment()
//            else -> BlankFragment.newInstance(mediaId)
        }


    override fun getPageTitle(position: Int): CharSequence {
        return context.getString(titles[position])
    }

    companion object {
        private val titles = arrayOf(
            R.string.overview,
            R.string.relation,
            R.string.character,
//            R.string.episodes,
            R.string.staff,
            R.string.reviews,
        )
    }
}