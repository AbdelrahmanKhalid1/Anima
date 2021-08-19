package com.ak.otaku_kun.ui.discover

import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListTabBinding
import com.ak.otaku_kun.ui.adapter.paging.DiscoverPagingAdapter
import com.ak.otaku_kun.ui.base.fragment.BaseFragment

class DiscoverFragment :
    BaseFragment<FragmentListTabBinding>(R.layout.fragment_list_tab) {

    //TODO check why loading data is not functioning well
    private lateinit var pagingAdapter: DiscoverPagingAdapter
    override fun setUpUI() {
        pagingAdapter = DiscoverPagingAdapter(requireActivity())
        binding.apply {
            viewPager.adapter = pagingAdapter
            pagingAdapter.connectTabWithPager(viewPager, tabs)
            viewPager.isUserInputEnabled = false
        }
    }

    override fun setObservers() {}
}