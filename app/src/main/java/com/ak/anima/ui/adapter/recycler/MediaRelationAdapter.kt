package com.ak.anima.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.ak.anima.databinding.ItemMediaMiniBinding
import com.ak.anima.databinding.ItemMediaRelationBinding
import com.ak.anima.model.index.Media
import com.ak.anima.ui.base.adapter.BaseAdapter
import com.ak.anima.ui.base.custom.BaseViewHolder
import com.ak.anima.utils.ItemClickHandler
import com.ak.anima.utils.OnMediaClick
import com.ak.anima.ui.adapter.recycler.MediaAdapter.MediaMiniViewHolder

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
                mediaAdapter.addItems(item.second)
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