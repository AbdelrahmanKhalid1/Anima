package com.ak.otaku_kun.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.otaku_kun.databinding.ItemUserBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.model.index.User
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder

class UserAdapter : BasePagingAdapter<User>(PHOTO_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<User> {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        BaseViewHolder<User>(binding.root) {
        override fun bind(item: User) {
            binding.user = item
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.equals(newItem)
        }
    }
}
