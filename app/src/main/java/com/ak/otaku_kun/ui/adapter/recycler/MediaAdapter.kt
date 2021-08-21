package com.ak.otaku_kun.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.otaku_kun.databinding.ItemMediaBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder

class MediaAdapter : BasePagingAdapter<Media>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    class MediaViewHolder(private val binding: ItemMediaBinding) :
        BaseViewHolder<Media>(binding.root) {
        override fun bind(item: Media) {
            binding.media = item
            //TODO in settings we can add option on how to view rating rather by 7.7 or 77% or 0.77
            binding.rate.text = "${item.averageScore}"
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean =
                oldItem.equals(newItem)
        }
    }
}