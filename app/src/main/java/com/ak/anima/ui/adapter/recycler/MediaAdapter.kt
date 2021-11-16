package com.ak.anima.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.anima.databinding.ItemMediaBinding
import com.ak.anima.databinding.ItemMediaMiniBinding
import com.ak.anima.model.index.Media
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.ak.anima.ui.base.custom.BaseMediaViewHolder
import com.ak.anima.ui.base.custom.BaseViewHolder
import com.ak.anima.utils.ItemClickHandler

class MediaAdapter(
    private var isSearch: Boolean = false,
    private val mediaClickHandler: ItemClickHandler? = null,
    private val isMyList: Boolean = false
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
            MediaViewHolder(binding, mediaClickHandler, isMyList)
        }
    }

    class MediaViewHolder(
        private val binding: ItemMediaBinding,
        mediaClickHandler: ItemClickHandler?,
        isMyList: Boolean
    ) :
        BaseMediaViewHolder<Media>(binding, mediaClickHandler) {
        init {
            binding.isMyList = isMyList
        }
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