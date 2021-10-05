package com.ak.otaku_kun.ui.discover

import android.os.Bundle
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListTabBinding
import com.ak.otaku_kun.ui.adapter.pager.DiscoverPagerAdapter
import com.ak.otaku_kun.ui.base.fragment.BaseFragment
import com.ak.otaku_kun.ui.interfaces.TabbedView
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.Keys

class DiscoverFragment :
    BaseFragment<FragmentListTabBinding>(R.layout.fragment_list_tab) {

    private lateinit var pagerAdapter: DiscoverPagerAdapter
    private lateinit var tabbedView: TabbedView
    private var viewPagerPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.run {
            viewPagerPosition = getInt(Keys.STATE_VIEW_PAGER_POSITION, 0)
        }
    }

    override fun setUpUI() {
        tabbedView = requireActivity() as TabbedView
        pagerAdapter = DiscoverPagerAdapter(requireActivity())
        binding.viewPager.apply {
            adapter = pagerAdapter
            isUserInputEnabled = false
            pagerAdapter.connectTabWithPager(this, tabbedView.getTabLayout())

            if (viewPagerPosition != 0)
                setCurrentItem(viewPagerPosition, false)
        }
    }

    override fun setObservers() {}

//    override fun onDetach() {
//        tabbedView.hideTabLayout()
//        super.onDetach()
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putInt(Keys.STATE_VIEW_PAGER_POSITION, binding.viewPager.currentItem)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}