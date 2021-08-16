package com.ak.otaku_kun.ui.base

import com.ak.otaku_kun.R
import android.view.View
import android.view.animation.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapter<I : Any>(ITEM_COMPARATOR: DiffUtil.ItemCallback<I>) :
    PagingDataAdapter<I, BaseAdapter.BaseViewHolder<I>>(ITEM_COMPARATOR) {

    private var lastPosition = -1

    override fun onBindViewHolder(holder: BaseViewHolder<I>, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
        setAnimation(holder.itemView, position)
    }

     private fun setAnimation(view: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.item_fall_down)
            view.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<I>) {
        holder.itemView.clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }

    abstract class BaseViewHolder<I>(root: View) :
        RecyclerView.ViewHolder(root) {
        abstract fun bind(item: I)
    }
}