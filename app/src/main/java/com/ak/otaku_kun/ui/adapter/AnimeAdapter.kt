package com.ak.otaku_kun.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.otaku_kun.databinding.ItemMediaBinding
import com.ak.otaku_kun.model.remote.index.Anime
import com.ak.otaku_kun.ui.base.BaseAdapter

class AnimeAdapter : BaseAdapter<Anime>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    class AnimeViewHolder(private val binding: ItemMediaBinding) :
        BaseViewHolder<Anime>(binding.root) {
        override fun bind(item: Anime) {
            binding.media = item
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Anime>() {
            override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem.equals(newItem)
        }
    }
}