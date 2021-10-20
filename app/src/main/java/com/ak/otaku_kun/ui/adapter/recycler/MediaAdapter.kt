package com.ak.otaku_kun.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.databinding.ItemMediaBinding
import com.ak.otaku_kun.databinding.ItemMediaMiniBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.custom.BaseMediaViewHolder
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder
import com.ak.otaku_kun.utils.ItemClickHandler

class MediaAdapter(
    private var isSearch: Boolean = false,
    private val mediaClickHandler: ItemClickHandler? = null
) : BasePagingAdapter<Media>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Media> {
        return if (isSearch) {
            val binding = ItemMediaMiniBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            MediaMiniViewHolder(binding, mediaClickHandler)
        } else {
            val binding =
                ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MediaViewHolder(binding, mediaClickHandler)
        }
    }

    class MediaViewHolder(
        private val binding: ItemMediaBinding,
        mediaClickHandler: ItemClickHandler? = null
    ) :
        BaseMediaViewHolder<Media>(binding, mediaClickHandler) {
        override fun bind(item: Media) {
            binding.media = item
        }

        override fun getMedia(): Media? = binding.media
    }

    class MediaMiniViewHolder(
        private val binding: ItemMediaMiniBinding,
        mediaClickHandler: ItemClickHandler? = null
    ) :
        BaseMediaViewHolder<Media>(binding, mediaClickHandler) {
        override fun bind(item: Media) {
            binding.apply {
                media = item
            }
        }
        override fun getMedia(): Media? = binding.media
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