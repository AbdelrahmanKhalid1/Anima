package com.ak.otaku_kun.ui.discover

import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListTabBinding
import com.ak.otaku_kun.ui.adapter.pager.DiscoverPagerAdapter
import com.ak.otaku_kun.ui.base.fragment.BaseFragment
import com.ak.otaku_kun.ui.interfaces.TabbedView

class DiscoverFragment :
    BaseFragment<FragmentListTabBinding>(R.layout.fragment_list_tab) {

    private lateinit var pagerAdapter: DiscoverPagerAdapter
    private lateinit var tabbedView: TabbedView
    override fun setUpUI() {
        tabbedView = requireActivity() as TabbedView
        pagerAdapter = DiscoverPagerAdapter(requireActivity())
        binding.apply {
            viewPager.adapter = pagerAdapter
            pagerAdapter.connectTabWithPager(viewPager, tabbedView.getTabLayout())
            viewPager.isUserInputEnabled = false
        }
    }

    override fun setObservers() {}

//    override fun onDetach() {
//        tabbedView.hideTabLayout()
//        super.onDetach()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}