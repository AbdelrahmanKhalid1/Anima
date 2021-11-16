package com.ak.anima.ui.base.fragment

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.ak.anima.databinding.CustomViewErrorBinding
import com.ak.anima.ui.adapter.DataLoadStateAdapter
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.ak.anima.utils.DataHandler
import kotlinx.android.synthetic.main.custom_view_error.view.*

private const val TAG = "BasePagingListFragment"

abstract class BasePagingListFragment<V : ViewDataBinding, I : Any>(layoutId: Int) :
    BaseFragment<V>(layoutId) {

    private val dataHandler = DataHandler.DataHandlerPaging<I>()

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
                    getErrorView().root.visibility = View.GONE
                    getProgressBar().visibility = View.VISIBLE
                }
                is LoadState.NotLoading -> {
                    getProgressBar().visibility = View.GONE
                    getErrorView().root.visibility = View.GONE
                }

                is LoadState.Error -> {
                    val error = it.refresh as LoadState.Error
                    getProgressBar().visibility = View.GONE
                    dataHandler.displayError(error, getErrorView(), requireContext())
                }
            }
        }

        getErrorView().btnError.setOnClickListener {
            getRecyclerAdapter().refresh()
        }
    }

    fun displayData(data: PagingData<I>?) {
//        val isSubmitted =
        dataHandler.displayData(data, getProgressBar(), getRecyclerAdapter(), lifecycle)
//        if (isSubmitted)
//            getErrorView().root.visibility = View.VISIBLE
    }

    abstract fun getRecycler(): RecyclerView
    abstract fun getRecyclerAdapter(): BasePagingAdapter<I>
    abstract fun getRecyclerLayoutManager(): RecyclerView.LayoutManager
    abstract fun getProgressBar(): View
    abstract fun getErrorView(): CustomViewErrorBinding

    override fun onDestroyView() {
        getRecycler().adapter = null
        super.onDestroyView()
    }
}
