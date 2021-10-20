package com.ak.otaku_kun.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ak.otaku_kun.databinding.ItemStaffBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.model.index.Staff
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder

class StaffAdapter : BasePagingAdapter<Staff>(PHOTO_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Staff> {
        val binding = ItemStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffViewHolder(binding)
    }

    class StaffViewHolder(private val binding: ItemStaffBinding) : BaseViewHolder<Staff>(binding.root){
        override fun bind(item: Staff) {
            binding.staff = item
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Staff>() {
            override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean =
                oldItem.equals(newItem)
        }
    }
}
