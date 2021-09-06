package com.ak.otaku_kun.ui.search.results.media

import android.os.Bundle
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.adapter.recycler.MediaAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.fragment.BasePagingListFragment
import com.ak.otaku_kun.utils.Const
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaFragment : BasePagingListFragment<FragmentListBinding, Media>(R.layout.fragment_list) {

    private lateinit var mediaAdapter: MediaAdapter
    private val viewModel: MediaViewModel by viewModels()

    override fun setUpUI() {
        arguments?.let {
            val mediaType = it.getSerializable(Const.KEY_MEDIA_TYPE) as MediaType
            val query = it.getString(Const.KEY_QUERY)
            query?.let {
                viewModel.findMediaByName(mediaType, query)
            }
        }

        mediaAdapter = MediaAdapter(true)
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.searchResult.observe(viewLifecycleOwner, { displayData(it) })
    }

    override fun getRecycler(): RecyclerView = binding.recycler

    override fun getRecyclerAdapter(): BasePagingAdapter<Media> = mediaAdapter

    override fun getProgressBar(): ProgressBar = binding.progressBar

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(requireContext(), 3)

    companion object {
        @JvmStatic
        fun newInstance(mediaType: MediaType, query: String): MediaFragment =
            MediaFragment().apply {
                val bundle = Bundle()
                bundle.putSerializable(Const.KEY_MEDIA_TYPE, mediaType)
                bundle.putString(Const.KEY_QUERY, query)
                arguments = bundle
            }
    }
}