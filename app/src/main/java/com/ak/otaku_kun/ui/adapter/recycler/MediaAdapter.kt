package com.ak.otaku_kun.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.databinding.ItemMediaBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder
import com.ak.otaku_kun.utils.ItemClickBehavior
import com.ak.otaku_kun.utils.ItemClickHandler

class MediaAdapter(
    private var isSearch: Boolean = false,
    private val mediaClickHandler: ItemClickHandler? = null
) : BasePagingAdapter<Media>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding, isSearch, mediaClickHandler)
    }

    class MediaViewHolder(
        private val binding: ItemMediaBinding,
        isSearch: Boolean = false,
        mediaClickHandler: ItemClickHandler? = null
    ) :
        BaseViewHolder<Media>(binding.root) {
        init {
            binding.mediaGenre.visibility = if (isSearch) View.GONE else View.VISIBLE
            mediaClickHandler?.run {
                binding.apply {
                    root.setOnClickListener {
                        if (media != null && bindingAdapterPosition != RecyclerView.NO_POSITION)
                            onItemClick(media!!.id, it)
                    }

                    root.setOnLongClickListener {
                        return@setOnLongClickListener if (media != null && bindingAdapterPosition != RecyclerView.NO_POSITION) {
//                            onItemClick(media!!.id, it)
                            true
                        }else false
                    }
                }
            }
        }

        override fun bind(item: Media) {
            binding.media = item
            //TODO in settings we can add option on how to view rating rather by 7.7 or 77% or 0.77
            binding.rate.text = item.averageScore
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