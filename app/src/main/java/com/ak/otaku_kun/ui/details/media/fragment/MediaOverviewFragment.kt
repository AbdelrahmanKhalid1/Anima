package com.ak.otaku_kun.ui.details.media.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentMediaOverviewBinding
import com.ak.otaku_kun.databinding.ItemTagBinding
import com.ak.otaku_kun.model.details.Media
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder
import com.ak.otaku_kun.ui.base.fragment.BaseFragment
import com.ak.otaku_kun.ui.details.media.MediaViewModel
import com.ak.otaku_kun.ui.view.YoutubeView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MediaOverview"

@AndroidEntryPoint
class MediaOverviewFragment :
    BaseFragment<FragmentMediaOverviewBinding>(R.layout.fragment_media_overview),
    TagsAdapter.OnTagClickListener {

    private val viewModel: MediaViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var tagsAdapter: TagsAdapter

    override fun setUpUI() {
        tagsAdapter = TagsAdapter(this)
        binding.tagsRecycler.apply {
            adapter = tagsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

    override fun setObservers() {
        viewModel.mediaLiveData.observe(requireActivity()) {
            it?.let { media ->
                binding.media = media
                setTrailerSection(media.trailer)
                tagsAdapter.setItems(media.tags)
            }
        }
    }

    private fun setTrailerSection(trailer: Media.Trailer?) {
        trailer?.run {
            childFragmentManager.beginTransaction()
                .replace(R.id.youtube_view, YoutubeView())
                .commit()
            binding.youtubeView.visibility = View.VISIBLE
        }
    }

    override fun onTagClick(position: Int) {
        val tag = viewModel.mediaLiveData.value?.tags?.get(position)
        tag?.run {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(name)
                .setIcon(R.drawable.ic_tag)
                .setMessage(description)
                .setPositiveButton("ok", null)
                .show()
        }
    }
}

private class TagsAdapter(private val listener: OnTagClickListener) : BaseAdapter<Media.Tag>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Media.Tag> {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding, listener)
    }

    class TagViewHolder(
        private val binding: ItemTagBinding,
        private val listener: OnTagClickListener
    ) :
        BaseViewHolder<Media.Tag>(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener.onTagClick(bindingAdapterPosition)
                }
            }
        }

        override fun bind(item: Media.Tag) {
            binding.tag = item
        }
    }

    interface OnTagClickListener {
        fun onTagClick(position: Int)
    }
}