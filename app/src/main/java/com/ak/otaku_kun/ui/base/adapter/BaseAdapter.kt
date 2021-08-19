package com.ak.otaku_kun.ui.base.adapter

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder

abstract class BaseAdapter<data: Any> : RecyclerView.Adapter<BaseViewHolder<data>>(){

    private var lastPosition = -1
    private var _items = ArrayList<data>()
    val items : List<data> get() = _items

    override fun onBindViewHolder(holder: BaseViewHolder<data>, position: Int) {
        holder.bind(_items[position])
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int = _items.size

    fun setItems(items : List<data>){
        _items = items as ArrayList<data>
        notifyDataSetChanged()
    }

    fun addItems(items: List<data>){
        _items.addAll(items)
        notifyDataSetChanged()
    }

    private fun setAnimation(view: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.item_fall_down)
//            animation.duration =
            view.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<data>) {
        holder.itemView.clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }
}