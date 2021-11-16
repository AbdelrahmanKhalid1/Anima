package com.ak.anima.ui.base.adapter

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ak.anima.R
import com.ak.anima.ui.base.custom.BaseViewHolder
import com.ak.anima.utils.Keys

abstract class BaseAdapter<data : Any> : RecyclerView.Adapter<BaseViewHolder<data>>() {

    private var lastPosition = -1
    private var _items = ArrayList<data>()
    val items: List<data> get() = _items

    override fun onBindViewHolder(holder: BaseViewHolder<data>, position: Int) {
        holder.bind(_items[position])
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int = _items.size

    open fun setItems(items: List<data>) {
        if (items.isNotEmpty()) {
            _items = items as ArrayList<data>
            notifyDataSetChanged()
        }
    }

    open fun addItems(items: List<data>) {
        _items.addAll(items)
        notifyDataSetChanged()
    }

    private fun setAnimation(view: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(view.context, R.anim.item_fall_down)
//            animation.duration =
            view.startAnimation(animation)
            lastPosition = position
        }
    }

    /**
     * This function will operate only in grouped adapters
     * it makes header layouts takes whole width of the screen
     * **/
    override fun onViewAttachedToWindow(holder: BaseViewHolder<data>) {
        super.onViewAttachedToWindow(holder)
        if (getItemViewType(holder.layoutPosition) == Keys.RECYCLER_TYPE_HEADER) {
            val layoutParams =
                holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = true
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<data>) {
        holder.itemView.clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }
}