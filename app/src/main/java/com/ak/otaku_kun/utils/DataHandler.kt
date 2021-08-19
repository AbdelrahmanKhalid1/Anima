package com.ak.otaku_kun.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.apollographql.apollo.exception.ApolloNetworkException

private const val TAG = "DataHandler"

abstract class DataHandler{

    fun displayProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    fun displayError(error: LoadState.Error, progressBar: ProgressBar, context: Context) {
        progressBar.visibility = View.GONE
        Log.e(TAG, "displayError: ", error.error)
        when (error.error) {
            is ApolloNetworkException -> {
                Log.e(TAG, "displayError: ", error.error)
                Toast.makeText(
                    context,
                    "Check internet connection",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    class DataHandlerPaging<I : Any> : DataHandler() {
        fun displayData(
            data: PagingData<I>?,
            progressBar: ProgressBar,
            adapter: BasePagingAdapter<I>,
            lifecycle: Lifecycle
        ) {
            progressBar.visibility = View.GONE
            data?.let {
                adapter.submitData(lifecycle, it)
                Log.d(TAG, "displayData: $it")
            }
            Log.d(TAG, "displayData: $data")
        }
    }

    class DataHandlerNotPaging<I: Any>: DataHandler(){
        fun displayData(data: List<I>, progressBar: ProgressBar, adapter: BaseAdapter<I>) {
            progressBar.visibility = View.GONE
            adapter.setItems(data)
            Log.d(TAG, "displayData: ${data.size}")
        }
    }

}