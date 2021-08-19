package com.ak.otaku_kun.ui.base.custom

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<I>(root: View) :
    RecyclerView.ViewHolder(root) {
    abstract fun bind(item: I)
}