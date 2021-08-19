package com.ak.otaku_kun.ui.browse

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.remote.index.Media
import com.ak.otaku_kun.ui.adapter.recycler.MediaAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.fragment.BasePagingListFragment
import com.ak.otaku_kun.ui.dialog.FilterQueryDialog
import com.ak.otaku_kun.ui.dialog.SortDialog
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.otaku_kun.utils.StateEvent
import com.ak.type.MediaSort
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint

const val MEDIA_TYPE = "mediaType"

@AndroidEntryPoint
class BrowseMediaFragment :
    BasePagingListFragment<FragmentListBinding, Media>(R.layout.fragment_list),
    SortDialog.SortDialogListener, FilterQueryDialog.OnFilterSaveClickListener {

    private val viewModel: BrowseMediaViewModel by viewModels()
    private lateinit var mediaAdapter: MediaAdapter

    override fun setUpUI() {
        setHasOptionsMenu(true)
        arguments?.let {
            viewModel.queryFilters.type = it.get(MEDIA_TYPE) as MediaType?
        }

        mediaAdapter = MediaAdapter()
        super.setUpUI()
        //getRecycler().scrollToPosition(viewModel.scrollPosition)
        val stateEvent =
            if (viewModel.queryFilters.type == MediaType.ANIME) StateEvent.LoadAnime else StateEvent.LoadManga
        viewModel.onTriggerStateEvent(stateEvent)

        mediaAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> if (mediaAdapter.itemCount == 0) {
                    dataHandler.displayProgressBar(getProgressBar())
                }
                is LoadState.NotLoading -> displayData(null)

                is LoadState.Error -> {
                    val error = it.refresh as LoadState.Error
                    handleError(error)
                }
            }
        }
    }

    override fun setObservers() {
        viewModel.dataState.observe(this, { displayData(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val filterQueryDialog =
                    FilterQueryDialog(viewModel.queryFilters, this)
                filterQueryDialog.show(childFragmentManager, "FilterQueryDialog")
                true
            }
            R.id.action_sort -> {
//                Log.d(TAG, "onResume: ${viewModel.queryFilters.printData()}")
                val sortDialog = SortDialog(viewModel.queryFilters.listSort!![0].rawValue, this)
                sortDialog.show(parentFragmentManager, "GenreDialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveClickListener(queryFilters: QueryFilters) {
        viewModel.queryFilters = queryFilters
        val stateEvent =
            if (viewModel.queryFilters.type == MediaType.ANIME) StateEvent.LoadAnime else StateEvent.LoadManga
        viewModel.onTriggerStateEvent(stateEvent)
        mediaAdapter = MediaAdapter()
        getRecycler().adapter = mediaAdapter
        setObservers()
    }

    override fun onSortOkClickListener(sort: List<MediaSort>) {
        viewModel.queryFilters.listSort = sort
        val stateEvent =
            if (viewModel.queryFilters.type == MediaType.ANIME) StateEvent.LoadAnime else StateEvent.LoadManga
        viewModel.onTriggerStateEvent(stateEvent)
        mediaAdapter = MediaAdapter()
        getRecycler().adapter = mediaAdapter
        setObservers()
    }

    override fun getRecycler(): RecyclerView = binding.recycler

    override fun getRecyclerAdapter(): BasePagingAdapter<Media> = mediaAdapter

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)

    override fun getProgressBar(): ProgressBar = binding.progressBar
}