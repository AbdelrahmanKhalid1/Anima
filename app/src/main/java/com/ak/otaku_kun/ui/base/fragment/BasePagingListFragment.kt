package com.ak.otaku_kun.ui.base.fragment

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.ui.adapter.DataLoadStateAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.utils.DataHandler
import kotlinx.android.synthetic.main.custom_view_error.view.*

private const val TAG = "BasePagingListFragment"

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
            getRecycler().adapter = getRecyclerAdapter()
            getRecycler().layoutManager = getRecyclerLayoutManager()
            getRecycler().setHasFixedSize(true)

        getRecyclerAdapter().addLoadStateListener {
            Log.d(TAG, "setUpUI: $it")
            when (it.refresh) {
                is LoadState.Loading -> if (getRecyclerAdapter().itemCount == 0) {
                    getErrorView().visibility = View.GONE
                    getProgressBar().visibility = View.VISIBLE
                }
                is LoadState.NotLoading -> {
                    getProgressBar().visibility = View.GONE
                    getErrorView().visibility = View.GONE
                }

                is LoadState.Error -> {
                    val error = it.refresh as LoadState.Error
                    getProgressBar().visibility = View.GONE
                    dataHandler.displayError(error, getErrorView(), requireContext())
                }
            }
        }

        getErrorView().findViewById<Button>(R.id.btn_error).setOnClickListener {
            getRecyclerAdapter().retry()
        }
    }

    fun displayData(data: PagingData<I>?) {
        dataHandler.displayData(data, getProgressBar(), getRecyclerAdapter(), lifecycle)
    }

    abstract fun getRecycler(): RecyclerView
    abstract fun getRecyclerAdapter(): BasePagingAdapter<I>
    abstract fun getRecyclerLayoutManager(): RecyclerView.LayoutManager
    abstract fun getProgressBar(): View
    abstract fun getErrorView(): View

    override fun onDestroyView() {
        getRecycler().adapter = null
        super.onDestroyView()
    }
}
