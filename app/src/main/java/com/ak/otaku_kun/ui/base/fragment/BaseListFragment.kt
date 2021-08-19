package com.ak.otaku_kun.ui.base.fragment

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.ui.adapter.DataLoadStateAdapter
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.utils.DataHandler
import com.apollographql.apollo.exception.ApolloNetworkException


abstract class BaseListFragment<V : ViewDataBinding, I : Any>(
    layoutId: Int
) : BaseFragment<V>(layoutId) {

    internal val dataHandler = DataHandler.DataHandlerNotPaging<I>()

    override fun setUpUI() {
//        val layoutAnimationController =
//            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation)
        getRecycler().apply {
            adapter = getRecyclerAdapter()
            layoutManager = getRecyclerLayoutManager()
            setHasFixedSize(true)
//            layoutAnimation = layoutAnimationController
//            scheduleLayoutAnimation()
        }
    }

    fun displayData(data: List<I>) {
        dataHandler.displayData(data, getProgressBar(), getRecyclerAdapter())
    }

    fun handleError(error: LoadState.Error) {
        dataHandler.displayError(error, getProgressBar(),requireContext())
    }

    abstract fun getRecycler(): RecyclerView
    abstract fun getRecyclerAdapter(): BaseAdapter<I>
    abstract fun getProgressBar(): ProgressBar
    abstract fun getRecyclerLayoutManager(): RecyclerView.LayoutManager
//    abstract fun getCustomErrorView() : View

    override fun onDestroyView() {
        getRecycler().adapter = null
        super.onDestroyView()
    }
}