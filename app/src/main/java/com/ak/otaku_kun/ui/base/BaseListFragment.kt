package com.ak.otaku_kun.ui.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.ui.adapter.DataLoadStateAdapter
import com.apollographql.apollo.exception.ApolloNetworkException
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

private const val TAG = "BaseListFragment"

abstract class BaseListFragment<ViewModel : androidx.lifecycle.ViewModel, V : ViewDataBinding, I : Any>(
    layoutId: Int
) : BaseFragment<ViewModel, V>(layoutId) {

    override fun setUpUI() {
//        val layoutAnimationController =
//            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation)

        getRecycler().apply {
            adapter = getRecyclerAdapter().withLoadStateHeaderAndFooter(
                header = DataLoadStateAdapter { getRecyclerAdapter().retry() },
                footer = DataLoadStateAdapter { getRecyclerAdapter().retry() }
            )
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
//            layoutAnimation = layoutAnimationController
//            scheduleLayoutAnimation()
        }
    }

    fun displayData(data: PagingData<I>?) {
        getProgressBar().visibility = View.GONE
        data?.let {
            getRecyclerAdapter().submitData(lifecycle, it)
//            getRecycler().scheduleLayoutAnimation()
        }
    }

    fun displayProgressBar() {
        getProgressBar().visibility = View.VISIBLE
    }

    fun displayError(error: LoadState.Error) {
        getProgressBar().visibility = View.GONE
        when (error.error) {
            is ApolloNetworkException -> {
                Log.e(TAG, "displayError: ", error.error)
                Toast.makeText(requireContext(), "Check internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    abstract fun getRecycler(): RecyclerView
    abstract fun getRecyclerAdapter(): BaseAdapter<I>
    abstract fun getProgressBar(): ProgressBar
//    abstract fun getCustomErrorView() : View

    override fun onDestroyView() {
        getRecycler().adapter = null;
        super.onDestroyView()
    }
}