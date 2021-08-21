package com.ak.otaku_kun.ui.discover

import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListTabBinding
import com.ak.otaku_kun.ui.adapter.paging.DiscoverPagingAdapter
import com.ak.otaku_kun.ui.base.fragment.BaseFragment
import com.ak.otaku_kun.ui.interfaces.TabbedView
import com.google.android.material.tabs.TabLayout

class DiscoverFragment :
    BaseFragment<FragmentListTabBinding>(R.layout.fragment_list_tab) {

    private lateinit var pagingAdapter: DiscoverPagingAdapter
    private lateinit var tabbedView: TabbedView
    override fun setUpUI() {
        tabbedView = requireActivity() as TabbedView
        pagingAdapter = DiscoverPagingAdapter(requireActivity())
        binding.apply {
            viewPager.adapter = pagingAdapter
            pagingAdapter.connectTabWithPager(viewPager, tabbedView.getTAbLayout())
            viewPager.isUserInputEnabled = false
        }
    }

    override fun setObservers() {}

    override fun onDestroyView() {
        tabbedView.hideTabLayout()
        super.onDestroyView()
    }
}