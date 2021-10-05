package com.ak.otaku_kun.ui.base.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.ui.adapter.DataLoadStateAdapter
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.DataHandler
import com.apollographql.apollo.exception.ApolloNetworkException
import kotlin.properties.Delegates


abstract class BaseListFragment<V : ViewDataBinding, I : Any>(
    layoutId: Int
) : BaseFragment<V>(layoutId) {

    private val dataHandler = DataHandler.DataHandlerNotPaging<I>()
  /*  private var recyclerItemPosition by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.run {
            recyclerItemPosition = getInt(Const.STATE_RECYCLER_POSITION, RecyclerView.NO_POSITION)
        }
    }
*/
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
        getProgressBar().visibility = View.GONE
        dataHandler.displayError(error, getErrorView(),requireContext())
    }
/*
    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            val layoutManager = getRecyclerLayoutManager() as LinearLayoutManager
            putInt(Const.STATE_RECYCLER_POSITION, layoutManager.findFirstCompletelyVisibleItemPosition())
        }
        super.onSaveInstanceState(outState)
    }*/

    override fun onDestroyView() {
        getRecycler().adapter = null
        super.onDestroyView()
    }

    abstract fun getRecycler(): RecyclerView
    abstract fun getRecyclerAdapter(): BaseAdapter<I>
    abstract fun getProgressBar(): View
    abstract fun getRecyclerLayoutManager(): RecyclerView.LayoutManager
    abstract fun getErrorView() : View
}