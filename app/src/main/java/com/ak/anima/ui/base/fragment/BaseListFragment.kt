package com.ak.anima.ui.base.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.ak.anima.databinding.CustomViewErrorBinding
import com.ak.anima.ui.base.adapter.BaseAdapter
import com.ak.anima.utils.Const
import com.ak.anima.utils.DataHandler
import com.ak.anima.utils.Keys

private const val TAG = "BaseListFragment"

abstract class BaseListFragment<V : ViewDataBinding, I : Any>(
    layoutId: Int,
) : BaseFragment<V>(layoutId) {

    private val dataHandler = DataHandler.DataHandlerNotPaging<I>()
    private var recyclerPosition = -1
    private var isLoading = true
    internal var isPager = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            recyclerPosition = it.getInt(Keys.STATE_RECYCLER_POSITION, Const.NO_ITEM)
        }
//        val layoutAnimationController =
//            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation)
        getRecycler().apply {
            adapter = getRecyclerAdapter()
            layoutManager = getRecyclerLayoutManager()
            setHasFixedSize(true)
            if (isPager)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
//                        Log.d(TAG, "onScrolled: $dx")

                        val totalItems = getRecyclerAdapter().itemCount
                        val visibleItems = recyclerView.childCount
                        val firstVisibleItem = getRecyclerFirstVisibleItem()
                        val visibleThreshold =
                            9 // minimum allowed threshold before next page reload request
                        if (!isLoading && totalItems - visibleItems <= firstVisibleItem + visibleThreshold) {
                            isLoading = true
                            loadMoreItems()
                        }
                    }
                })
            scrollToPosition(recyclerPosition)
//            layoutAnimation = layoutAnimationController
//            scheduleLayoutAnimation()
        }
    }

    fun displayData(data: List<I>) {
        dataHandler.displayData(data, getProgressBar(), getRecyclerAdapter())
        isLoading = false
    }

    fun handleError(error: LoadState.Error) {
        getProgressBar().visibility = View.GONE
        dataHandler.displayError(error, getErrorView(), requireContext())
        isLoading = false
    }

    abstract fun loadMoreItems()

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(Keys.STATE_RECYCLER_POSITION, getRecyclerFirstVisibleItem())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        getRecycler().adapter = null
        super.onDestroyView()
    }

    abstract fun getRecycler(): RecyclerView
    abstract fun getRecyclerAdapter(): BaseAdapter<I>
    abstract fun getRecyclerFirstVisibleItem(): Int
    abstract fun getProgressBar(): View
    abstract fun getRecyclerLayoutManager(): RecyclerView.LayoutManager
    abstract fun getErrorView(): CustomViewErrorBinding
}