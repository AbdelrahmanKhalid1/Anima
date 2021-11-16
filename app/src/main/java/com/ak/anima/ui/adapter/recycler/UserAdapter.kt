package com.ak.anima.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.anima.databinding.ItemUserBinding
import com.ak.anima.model.index.User
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.ak.anima.ui.base.custom.BaseViewHolder

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
