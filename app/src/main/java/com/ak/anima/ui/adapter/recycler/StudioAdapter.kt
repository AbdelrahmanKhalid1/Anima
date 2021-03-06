package com.ak.anima.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.anima.databinding.ItemStudioBinding
import com.ak.anima.model.index.Studio
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.ak.anima.ui.base.custom.BaseViewHolder

class StudioAdapter : BasePagingAdapter<Studio>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Studio> {
        val binding = ItemStudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudioViewHolder(binding)
    }

    class StudioViewHolder(private val binding: ItemStudioBinding) : BaseViewHolder<Studio>(binding.root){
        override fun bind(item: Studio) {
            binding.studio = item
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Studio>() {
            override fun areItemsTheSame(oldItem: Studio, newItem: Studio): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Studio, newItem: Studio): Boolean =
                oldItem.equals(newItem)
        }
    }
}
