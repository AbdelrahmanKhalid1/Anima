package com.ak.otaku_kun.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.databinding.ItemDiscoverBinding
import com.ak.otaku_kun.databinding.ItemMediaHorizontalBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.custom.BaseMediaViewHolder
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder
import com.ak.otaku_kun.utils.ItemClickHandler

class DiscoverAdapter(
    private val type: String,
    private val mediaClickHandler: ItemClickHandler? = null
) : BaseAdapter<List<Media>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<List<Media>> {
        val binding =
            ItemDiscoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscoverMediaViewHolder(binding, type, mediaClickHandler)
    }

    class DiscoverMediaViewHolder(
        private val binding: ItemDiscoverBinding,
        private val mediaType: String,
        mediaClickHandler: ItemClickHandler?
    ) : BaseViewHolder<List<Media>>(binding.root) {
        init {
            val mediaAdapter = MediaAdapter(mediaClickHandler)
            binding.recycler.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = mediaAdapter
                setHasFixedSize(true)
            }
        }
        override fun bind(item: List<Media>) {
            val mediaAdapter: MediaAdapter
            binding.apply {
                position = bindingAdapterPosition
                type = mediaType
                mediaAdapter = binding.recycler.adapter as MediaAdapter
                mediaAdapter.setItems(item)
            }
        }
    }

    class MediaAdapter(private val mediaClickHandler: ItemClickHandler?) : BaseAdapter<Media>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Media> {
            val binding = ItemMediaHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MediaViewHolder(binding, mediaClickHandler)
        }
        class MediaViewHolder(
            private val binding: ItemMediaHorizontalBinding,
            mediaClickHandler: ItemClickHandler?
        ) :
            BaseMediaViewHolder<Media>(binding, mediaClickHandler) {
            override fun bind(item: Media) {
                binding.media = item
            }
            override fun getMedia(): Media? = binding.media
        }
    }

    //TODO implement discover click
//    interface OnDiscoverClickListener {
//        fun onDiscoverClick(position: Int)
//    }
}
