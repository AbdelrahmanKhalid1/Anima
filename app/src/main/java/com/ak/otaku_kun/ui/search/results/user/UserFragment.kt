package com.ak.otaku_kun.ui.search.results.user

import android.os.Bundle
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.index.Staff
import com.ak.otaku_kun.model.index.User
import com.ak.otaku_kun.ui.adapter.recycler.StaffAdapter
import com.ak.otaku_kun.ui.adapter.recycler.UserAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.fragment.BasePagingListFragment
import com.ak.otaku_kun.ui.search.results.staff.StaffViewModel
import com.ak.otaku_kun.utils.Const
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment :  BasePagingListFragment<FragmentListBinding, User>(R.layout.fragment_list)  {

    private lateinit var userAdapter : UserAdapter
    private val viewModel: UserViewModel by viewModels()

    override fun setUpUI() {
        arguments?.let {
            val query = it.getString(Const.KEY_QUERY)
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

    override fun getProgressBar(): ProgressBar = binding.progressBar

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

    companion object{
        @JvmStatic
        fun newInstance(query: String): UserFragment = UserFragment().apply {
            val args = Bundle()
            args.putString(Const.KEY_QUERY, query)
            arguments = args
        }
    }
}
