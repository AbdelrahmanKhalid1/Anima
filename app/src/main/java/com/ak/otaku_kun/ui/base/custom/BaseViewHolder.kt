package com.ak.otaku_kun.ui.base.custom

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.utils.ItemClickHandler

abstract class BaseViewHolder<I>(root: View) :
    RecyclerView.ViewHolder(root) {
    abstract fun bind(item: I)
}

abstract class BaseMediaViewHolder<data>(binding: ViewDataBinding, mediaClickHandler: ItemClickHandler?) :
    BaseViewHolder<data>(binding.root) {
    init {
        mediaClickHandler?.run {
            binding.apply {
                root.setOnClickListener {
                    if (getMedia() != null && bindingAdapterPosition != RecyclerView.NO_POSITION)
                        onItemClick(getMedia()!!.id, it)
                }
                root.setOnLongClickListener {
                    return@setOnLongClickListener getMedia() != null && bindingAdapterPosition != RecyclerView.NO_POSITION
                }
            }
        }
    }

    abstract fun getMedia(): Media?
}