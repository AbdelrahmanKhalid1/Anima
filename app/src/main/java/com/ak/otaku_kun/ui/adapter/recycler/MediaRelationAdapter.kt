package com.ak.otaku_kun.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.ak.otaku_kun.databinding.ItemMediaMiniBinding
import com.ak.otaku_kun.databinding.ItemMediaRelationBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder
import com.ak.otaku_kun.utils.ItemClickHandler
import com.ak.otaku_kun.utils.OnMediaClick
import com.ak.otaku_kun.ui.adapter.recycler.MediaAdapter.MediaMiniViewHolder

class MediaRelationAdapter : BaseAdapter<Pair<String, List<Media>>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaRelationViewHolder {
        val binding =
            ItemMediaRelationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaRelationViewHolder(binding)
    }

    class MediaRelationViewHolder(
        private val binding: ItemMediaRelationBinding
    ) : BaseViewHolder<Pair<String, List<Media>>>(binding.root) {
        init {
            val mediaClick = OnMediaClick()
            val mediaClickHandler = ItemClickHandler(binding.root.context, mediaClick)
            val mediaAdapter = MediaAdapter(mediaClickHandler)
            binding.recycler.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = mediaAdapter
            }
        }

        override fun bind(item: Pair<String, List<Media>>) {
            val mediaAdapter: MediaAdapter
            binding.apply {
                count = item.second.size
                relation = item.first
                mediaAdapter = binding.recycler.adapter as MediaAdapter
                mediaAdapter.setItems(item.second)
            }
        }
    }

    class MediaAdapter(private val mediaClickHandler: ItemClickHandler? = null) :
        BaseAdapter<Media>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Media> {
            val binding = ItemMediaMiniBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MediaMiniViewHolder(binding, mediaClickHandler)
        }
    }
}