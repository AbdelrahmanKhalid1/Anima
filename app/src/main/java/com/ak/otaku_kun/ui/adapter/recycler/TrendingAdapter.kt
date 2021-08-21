package com.ak.otaku_kun.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ak.otaku_kun.databinding.ItemMediaBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder

class TrendingAdapter : BaseAdapter<Media>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Media> {
        val binding =
            ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaAdapter.MediaViewHolder(binding)
    }
}
