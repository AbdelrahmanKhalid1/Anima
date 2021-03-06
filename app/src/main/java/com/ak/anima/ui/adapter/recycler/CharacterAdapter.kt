package com.ak.anima.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.anima.databinding.ItemCharacterBinding
import com.ak.anima.model.index.Character
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.ak.anima.ui.base.custom.BaseViewHolder

class CharacterAdapter : BasePagingAdapter<Character>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Character> {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    class CharacterViewHolder(private val binding: ItemCharacterBinding) : BaseViewHolder<Character>(binding.root){
        override fun bind(item: Character) {
            binding.character = item
        }

    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.equals(newItem)
        }
    }
}