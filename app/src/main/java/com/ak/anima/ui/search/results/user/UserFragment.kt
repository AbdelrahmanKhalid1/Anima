package com.ak.anima.ui.search.results.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.anima.R
import com.ak.anima.databinding.CustomViewErrorBinding
import com.ak.anima.databinding.FragmentListBinding
import com.ak.anima.model.index.User
import com.ak.anima.ui.adapter.recycler.UserAdapter
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.ak.anima.ui.base.fragment.BasePagingListFragment
import com.ak.anima.utils.Keys
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment :  BasePagingListFragment<FragmentListBinding, User>(R.layout.fragment_list)  {

    private lateinit var userAdapter : UserAdapter
    private val viewModel: UserViewModel by viewModels()

    override fun setUpUI() {
        arguments?.let {
            val query = it.getString(Keys.KEY_QUERY)
            query?.let {
                viewModel.searchStaff(query)
            }
        }

        userAdapter = UserAdapter()
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.searchResult.observe(viewLifecycleOwner, {displayData(it)})
    }

    override fun getRecycler(): RecyclerView = binding.recycler

    override fun getRecyclerAdapter(): BasePagingAdapter<User> = userAdapter

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

    override fun getProgressBar(): View = binding.customViewLoad
    override fun getErrorView(): CustomViewErrorBinding = binding.viewError

    companion object{
        @JvmStatic
        fun newInstance(query: String): UserFragment = UserFragment().apply {
            val args = Bundle()
            args.putString(Keys.KEY_QUERY, query)
            arguments = args
        }
    }
}
