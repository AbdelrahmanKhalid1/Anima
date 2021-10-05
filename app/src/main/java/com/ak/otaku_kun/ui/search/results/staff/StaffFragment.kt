package com.ak.otaku_kun.ui.search.results.staff

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.index.Staff
import com.ak.otaku_kun.ui.adapter.recycler.StaffAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.fragment.BasePagingListFragment
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.Keys
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StaffFragment : BasePagingListFragment<FragmentListBinding, Staff>(R.layout.fragment_list) {

    private lateinit var staffAdapter : StaffAdapter
    private val viewModel: StaffViewModel by viewModels()

    override fun setUpUI() {
        arguments?.let {
            val query = it.getString(Keys.KEY_QUERY)
            query?.let {
                viewModel.searchStaff(query)
            }
        }

        staffAdapter = StaffAdapter()
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.searchResult.observe(viewLifecycleOwner, {displayData(it)})
    }

    override fun getRecycler(): RecyclerView  = binding.recycler

    override fun getRecyclerAdapter(): BasePagingAdapter<Staff> = staffAdapter

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(requireContext(), Const.RECYCLER_GRID_SEARCH_SPAN_COUNT)

    override fun getProgressBar(): View = binding.progressBar
    override fun getErrorView(): View = binding.viewError

    companion object{
        @JvmStatic
        fun newInstance(query: String): StaffFragment = StaffFragment().apply {
            val args = Bundle()
            args.putString(Keys.KEY_QUERY, query)
            arguments = args
        }
    }
}
