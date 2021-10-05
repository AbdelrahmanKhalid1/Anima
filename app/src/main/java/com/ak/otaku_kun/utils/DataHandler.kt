package com.ak.otaku_kun.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.ak.otaku_kun.R
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.apollographql.apollo.exception.ApolloNetworkException

private const val TAG = "DataHandler"

abstract class DataHandler {

    fun displayError(error: LoadState.Error, errorView: View, context: Context) {
        errorView.visibility = View.VISIBLE
        val errorText = errorView.findViewById<TextView>(R.id.text_error)
        val errorBtn = errorView.findViewById<Button>(R.id.btn_error)
        when (error.error) {
            is ApolloNetworkException -> {
                errorBtn.visibility = View.VISIBLE
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                Log.e(TAG, "displayError: $connectivityManager ", error.error)
                errorText.text = if(!connectivityManager.isActiveNetworkMetered){ //if phone not connected to internet
                     context.getString(R.string.error_connection)
                }else{
                    error.error.message
                }
            }
            is EmptyDataException -> {
                errorText.text = error.error.message
                errorBtn.visibility = View.GONE
            }
        }
    }

    class DataHandlerPaging<I : Any> : DataHandler() {
        fun displayData(
            data: PagingData<I>?,
            progressBar: View,
            adapter: BasePagingAdapter<I>,
            lifecycle: Lifecycle
        ) {
            progressBar.visibility = View.GONE
            data?.let {
                adapter.submitData(lifecycle, it)
                adapter.retry()
            }
            Log.d(TAG, "displayData: ${adapter.itemCount}")
            Log.d(TAG, "displayData: $data")
        }
    }

    class DataHandlerNotPaging<I : Any> : DataHandler() {
        fun displayData(data: List<I>, progressBar: View, adapter: BaseAdapter<I>) {
            progressBar.visibility = View.GONE
            adapter.setItems(data)
            Log.d(TAG, "displayData: ${data.size}")
        }
    }

}