package com.ak.anima.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.ak.anima.R
import com.ak.anima.databinding.CustomViewErrorBinding
import com.ak.anima.ui.base.adapter.BaseAdapter
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.apollographql.apollo.exception.ApolloNetworkException

private const val TAG = "DataHandler"

abstract class DataHandler {

    fun displayError(
        error: LoadState.Error,
        errorBinding: CustomViewErrorBinding,
        context: Context,
    ) {
        errorBinding.run {
            root.visibility = View.VISIBLE
            when (error.error) {
                is ApolloNetworkException -> {
                    btnError.visibility = View.VISIBLE
                    val connectivityManager =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    Log.e(TAG, "displayError: $connectivityManager ", error.error)
                    textError.text =
                        if (!connectivityManager.isActiveNetworkMetered) { //if phone not connected to internet
                            context.getString(R.string.error_connection)
                        } else {
                            error.error.message
                        }
                }
                is EmptyDataException -> {
                    textError.text = error.error.message
                    btnError.visibility = View.GONE
                }
            }
        }
    }

    class DataHandlerPaging<I : Any> : DataHandler() {
        fun displayData(
            data: PagingData<I>?,
            progressBar: View,
            adapter: BasePagingAdapter<I>,
            lifecycle: Lifecycle,
        ):Boolean {
            progressBar.visibility = View.GONE
            data?.let {
                adapter.submitData(lifecycle, it)
                adapter.retry()
                return true
            }
//            Log.d(TAG, "displayData: ${adapter.itemCount}")
//            Log.d(TAG, "displayData: $data")
            return false
        }
    }

    class DataHandlerNotPaging<I : Any> : DataHandler() {
        fun displayData(data: List<I>, progressBar: View, adapter: BaseAdapter<I>) {
            progressBar.visibility = View.GONE
            adapter.addItems(data)
            Log.d(TAG, "displayData: ${data.size}")
        }
    }

}