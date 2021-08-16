package com.ak.otaku_kun.ui.browse.anime

import android.os.Bundle
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.remote.index.Anime
import com.ak.otaku_kun.ui.adapter.AnimeAdapter
import com.ak.otaku_kun.ui.base.BaseAdapter
import com.ak.otaku_kun.ui.base.BaseListFragment
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.otaku_kun.utils.StateEvent
import com.ak.type.MediaSeason
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint

const val QUERY_FILTERS = "QueryFilter"

@AndroidEntryPoint
class BrowseAnimeFragment :
    BaseListFragment<BrowseAnimeViewModel, FragmentListBinding, Anime>(R.layout.fragment_list) {

    private val viewModel: BrowseAnimeViewModel by viewModels()
    private lateinit var queryFilters: QueryFilters
    private lateinit var animeAdapter: AnimeAdapter

    override fun setUpUI() {
        arguments?.apply {
            queryFilters = getParcelable(QUERY_FILTERS)!!
        }
        animeAdapter = AnimeAdapter()
//        Toast.makeText(requireContext(), queryFilters.printData(), Toast.LENGTH_SHORT)
        super.setUpUI()
        //getRecycler().scrollToPosition(viewModel.scrollPosition)

        viewModel.onTriggerStateEvent(StateEvent.LoadData, queryFilters)

        animeAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> if (animeAdapter.itemCount == 0) {
                    displayProgressBar()
                }
                is LoadState.NotLoading -> displayData(null)
//
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

    override fun getRecyclerAdapter(): BaseAdapter<Anime> = animeAdapter

    override fun getProgressBar(): ProgressBar = binding.progressBar

    companion object {
        @JvmStatic
        fun newInstance(mediaSeason: MediaSeason, queryFilters: QueryFilters) =
            BrowseAnimeFragment().apply {
                arguments = Bundle().apply {
                    queryFilters.type = MediaType.ANIME
                    queryFilters.season = mediaSeason
                    putParcelable(QUERY_FILTERS, queryFilters)
                }
            }
    }
}