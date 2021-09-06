package com.ak.otaku_kun.ui.trending

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListTabBinding
import com.ak.otaku_kun.ui.base.fragment.BaseFragment
import com.ak.otaku_kun.ui.interfaces.TabbedView
import com.ak.otaku_kun.utils.Const
import com.ak.type.MediaType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TrendingFragment :  BaseFragment<FragmentListTabBinding>(R.layout.fragment_list_tab){

    private lateinit var pagingAdapter: TrendingPagingAdapter
    private lateinit var tabbedView: TabbedView
    override fun setUpUI() {
        tabbedView = requireActivity() as TabbedView
        pagingAdapter = TrendingPagingAdapter(requireActivity())
        binding.apply {
            viewPager.adapter = pagingAdapter
            pagingAdapter.connectTabWithPager(viewPager, tabbedView.getTabLayout())
        }
    }

    override fun setObservers() {}

//    override fun onDestroyView() {
//        tabbedView.hideTabLayout()
//        super.onDestroyView()
//    }

    class TrendingPagingAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        fun connectTabWithPager(viewPager: ViewPager2, tabLayout: TabLayout){
            TabLayoutMediator(tabLayout, viewPager){tab, position->
                tab.text = Const.TRENDING_TAB_TITLES[position]
            }.attach()
        }

        override fun createFragment(position: Int): Fragment =
            when(position) {
                0 -> TrendingMediaFragment.newInstance(MediaType.ANIME)
                else -> TrendingMediaFragment.newInstance(MediaType.MANGA)
            }
        override fun getItemCount(): Int = Const.TRENDING_TAB_TITLES.size
    }
}