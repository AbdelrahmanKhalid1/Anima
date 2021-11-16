package com.ak.anima.ui.base.adapter

import com.ak.anima.R
import android.view.View
import android.view.animation.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ak.anima.ui.base.custom.BaseViewHolder

abstract class BasePagingAdapter<I : Any>(ITEM_COMPARATOR: DiffUtil.ItemCallback<I>) :
    PagingDataAdapter<I, BaseViewHolder<I>>(ITEM_COMPARATOR) {

    private var lastPosition = -1

    override fun onBindViewHolder(holder: BaseViewHolder<I>, position: Int) {
        setAnimation(holder.itemView, position)
        getItem(position)?.let {
            holder.bind(it)
        }
    }

     private fun setAnimation(view: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.item_fall_down)
            animation.duration = 1000
            view.startAnimation(animation)
            lastPosition = position
        }else{
            view.animation = null
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<I>) {
        holder.itemView.clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }
}