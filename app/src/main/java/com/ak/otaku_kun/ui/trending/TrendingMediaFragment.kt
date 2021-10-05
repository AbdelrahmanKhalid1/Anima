package com.ak.otaku_kun.ui.trending

import android.os.Bundle
import android.view.View
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
import com.ak.otaku_kun.utils.Keys
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TrendingMediaFragment :
    BasePagingListFragment<FragmentListBinding, Media>(R.layout.fragment_list) {

    private val viewModel: TrendingViewModel by viewModels()
    private lateinit var mediaAdapter: MediaAdapter
    private lateinit var mediaType: MediaType

    override fun setUpUI() {
        arguments?.let {
            mediaType = it.get(Keys.KEY_MEDIA_TYPE) as MediaType
            //dataHandler.displayProgressBar(getProgressBar())
            if (viewModel.data.value == null)
                viewModel.getTrendingMedias(mediaType)
        }
        mediaAdapter = MediaAdapter()
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.data.observe(viewLifecycleOwner, { displayData(it) })
    }

    override fun getRecycler(): RecyclerView = binding.recycler

    override fun getRecyclerAdapter(): BasePagingAdapter<Media> = mediaAdapter

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(requireContext(), Const.RECYCLER_GRID_SPAN_COUNT)

    override fun getProgressBar(): View = binding.progressBar
    override fun getErrorView(): View = binding.viewError

    companion object {
        @JvmStatic
        fun newInstance(mediaType: MediaType): TrendingMediaFragment =
            TrendingMediaFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Keys.KEY_MEDIA_TYPE, mediaType)
                }
            }
    }
}