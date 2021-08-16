package com.ak.otaku_kun.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.otaku_kun.databinding.ItemMediaBinding
import com.ak.otaku_kun.model.remote.index.Media
import com.ak.otaku_kun.ui.base.BaseAdapter

class MediaAdapter : BaseAdapter<Media>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    class MediaViewHolder(private val binding: ItemMediaBinding) :
        BaseViewHolder<Media>(binding.root) {
        override fun bind(item: Media) {
            binding.media = item
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