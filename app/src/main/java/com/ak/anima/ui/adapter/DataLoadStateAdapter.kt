package com.ak.anima.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ak.anima.databinding.LoadStateFooterBinding

private const val TAG = "DataLoadStateAdapter"
class DataLoadStateAdapter(private val retry:() -> Unit) : LoadStateAdapter<DataLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                root.visibility = if(loadState is LoadState.Loading) View.VISIBLE else View.GONE
//                progressBar.isVisible = loadState is LoadState.Loading
//                textViewError.isVisible = loadState is LoadState.Loading
//                buttonRetry.isVisible = loadState is LoadState.Loading
            }
        }
    }
}