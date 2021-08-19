package com.ak.otaku_kun.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.databinding.ItemDiscoverBinding
import com.ak.otaku_kun.databinding.ItemMediaHorizontalBinding
import com.ak.otaku_kun.model.remote.index.Media
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder

class DiscoverAdapter(private val type: String) : BaseAdapter<List<Media>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<List<Media>> {
        val binding =
            ItemDiscoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscoverMediaViewHolder(binding, type)
    }

    class DiscoverMediaViewHolder(
        private val binding: ItemDiscoverBinding,
        private val mediaType: String
    ) : BaseViewHolder<List<Media>>(binding.root) {
        override fun bind(item: List<Media>) {
            val mediaAdapter = MediaAdapter()
            mediaAdapter.setItems(item)
            binding.apply {
                position = bindingAdapterPosition
                type = mediaType
                recycler.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    adapter = mediaAdapter
                    setHasFixedSize(true)
                }
            }
        }
    }

    class MediaAdapter : BaseAdapter<Media>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Media> {
            val binding = ItemMediaHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MediaViewHolder(binding)
        }

        class MediaViewHolder(private val binding: ItemMediaHorizontalBinding) :
            BaseViewHolder<Media>(binding.root) {
            override fun bind(item: Media) {
                binding.media = item
            }
        }

    }

}
