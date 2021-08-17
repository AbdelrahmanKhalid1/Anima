package com.ak.otaku_kun.ui.browse

import android.util.Log
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.remote.index.Media
import com.ak.otaku_kun.ui.adapter.MediaAdapter
import com.ak.otaku_kun.ui.base.BaseListFragment
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.otaku_kun.utils.StateEvent
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint

const val MEDIA_TYPE = "mediaType"

@AndroidEntryPoint
class BrowseMediaFragment :
    BaseListFragment<BrowseMediaViewModel, FragmentListBinding, Media>(R.layout.fragment_list) {

    private val viewModel: BrowseMediaViewModel by viewModels()
    private var queryFilters: QueryFilters = QueryFilters()
    private lateinit var mediaAdapter: MediaAdapter

    override fun setUpUI() {
        arguments?.apply {
            queryFilters.type = get(MEDIA_TYPE) as MediaType?
        }

        mediaAdapter = MediaAdapter()
        super.setUpUI()
        //getRecycler().scrollToPosition(viewModel.scrollPosition)
        val stateEvent =
            if (queryFilters.type == MediaType.ANIME) StateEvent.LoadAnime else StateEvent.LoadManga
        viewModel.onTriggerStateEvent(stateEvent, queryFilters)

        mediaAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> if (mediaAdapter.itemCount == 0) {
                    displayProgressBar()
                }
                is LoadState.NotLoading -> displayData(null)

                is LoadState.Error -> {
                    val error = it.refresh as LoadState.Error
                    displayError(error)
                }
            }
        }
    }

    override fun setObservers() {
        viewModel.dataState.observe(this, { displayData(it) })
    }

    override fun getRecycler(): RecyclerView = binding.recycler

    override fun getRecyclerAdapter(): MediaAdapter = mediaAdapter

    override fun getProgressBar(): ProgressBar = binding.progressBar

    companion object {
        @JvmStatic
        fun newInstance(mediaType: MediaType): BrowseMediaFragment =
            BrowseMediaFragment().apply {
                requireArguments().putSerializable(MEDIA_TYPE, mediaType)
            }
    }
}