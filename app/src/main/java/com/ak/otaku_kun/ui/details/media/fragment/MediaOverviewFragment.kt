package com.ak.otaku_kun.ui.details.media.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentMediaOverviewBinding
import com.ak.otaku_kun.databinding.ItemTagBinding
import com.ak.otaku_kun.model.details.Media
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder
import com.ak.otaku_kun.ui.base.fragment.BaseFragment
import com.ak.otaku_kun.ui.details.media.MediaViewModel
import com.ak.otaku_kun.ui.view.YoutubeView
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MediaOverview"
@AndroidEntryPoint
class MediaOverviewFragment :
    BaseFragment<FragmentMediaOverviewBinding>(R.layout.fragment_media_overview) {

    private val viewModel: MediaViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var tagsAdapter: TagsAdapter

    override fun setUpUI() {
        tagsAdapter = TagsAdapter()
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
}

private class TagsAdapter : BaseAdapter<Media.Tag>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Media.Tag> {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    class TagViewHolder(private val binding: ItemTagBinding) :
        BaseViewHolder<Media.Tag>(binding.root) {
        override fun bind(item: Media.Tag) {
            binding.tag = item
        }
    }
}