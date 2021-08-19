package com.ak.otaku_kun.ui.base.fragment

import android.widget.ProgressBar
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.ui.adapter.DataLoadStateAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.utils.DataHandler

abstract class BasePagingListFragment<V : ViewDataBinding, I : Any>(layoutId: Int) :
    BaseFragment<V>(layoutId) {

    internal val dataHandler = DataHandler.DataHandlerPaging<I>()

    override fun setUpUI() {
        getRecyclerAdapter().apply {
            withLoadStateHeaderAndFooter(
                header = DataLoadStateAdapter { getRecyclerAdapter().retry() },
                footer = DataLoadStateAdapter { getRecyclerAdapter().retry() }
            )
        }
        getRecycler().apply {
            adapter = getRecyclerAdapter()
            layoutManager = getRecyclerLayoutManager()
            setHasFixedSize(true)
        }
    }

    fun displayData(data: PagingData<I>?) {
        dataHandler.displayData(data, getProgressBar(), getRecyclerAdapter(), lifecycle)
    }

    fun handleError(error: LoadState.Error) {
        dataHandler.displayError(error, getProgressBar(),requireContext())
    }

    abstract fun getRecycler(): RecyclerView
    abstract fun getRecyclerAdapter(): BasePagingAdapter<I>
    abstract fun getProgressBar(): ProgressBar
    abstract fun getRecyclerLayoutManager(): RecyclerView.LayoutManager

    override fun onDestroyView() {
        getRecycler().adapter = null
        super.onDestroyView()
    }
}
