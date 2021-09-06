package com.ak.otaku_kun.ui.browse

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavGraph
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.adapter.recycler.MediaAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.fragment.BasePagingListFragment
import com.ak.otaku_kun.ui.dialog.FilterQueryDialog
import com.ak.otaku_kun.ui.dialog.SortDialog
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.otaku_kun.utils.StateEvent
import com.ak.type.MediaSort
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint

const val MEDIA_TYPE = "mediaType"
private const val TAG = "BrowseMediaFragment"

@AndroidEntryPoint
class BrowseMediaFragment :
    BasePagingListFragment<FragmentListBinding, Media>(R.layout.fragment_list),
    SortDialog.SortDialogListener, FilterQueryDialog.OnFilterSaveClickListener {

    private val viewModel: BrowseMediaViewModel by viewModels()
    private lateinit var mediaAdapter: MediaAdapter

    override fun setUpUI() {
        setHasOptionsMenu(true)
        arguments?.let {
            val mediaType = it.get(MEDIA_TYPE) as MediaType?
            mediaType?.let {
                viewModel.setQueryFilters(mediaType)
                viewModel.onTriggerStateEvent(StateEvent.LoadMedia)
            }
        }

        mediaAdapter = MediaAdapter()
        super.setUpUI()
        //getRecycler().scrollToPosition(viewModel.scrollPosition)

        mediaAdapter.addLoadStateListener {
            Log.d(TAG, "setUpUI: $it")
            when (it.refresh) {
                is LoadState.Loading -> if (mediaAdapter.itemCount == 0) {
                    dataHandler.displayProgressBar(getProgressBar())
                }
                is LoadState.NotLoading -> getProgressBar().visibility = View.GONE

                is LoadState.Error -> {
                    val error = it.refresh as LoadState.Error
                    handleError(error)
                }
            }
        }
    }

    override fun setObservers() {
        viewModel.queryFilters.observe(
            viewLifecycleOwner,
            {
                viewModel.onTriggerStateEvent(StateEvent.LoadMedia)
                viewModel.mediaData.observe(viewLifecycleOwner, { displayData(it) })
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.browse_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_filter -> {
                val filterQueryDialog =
                    FilterQueryDialog(viewModel.getQueryFilters(), this)
                filterQueryDialog.show(childFragmentManager, "FilterQueryDialog")
                true
            }
            R.id.action_sort -> {
                val sortDialog =
                    SortDialog(viewModel.getQueryFilters().listSort!![0].rawValue, this)
                sortDialog.show(parentFragmentManager, "GenreDialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveClickListener(queryFilters: QueryFilters) {
        viewModel.setQueryFilters(queryFilters)
        Log.d(TAG, "onSaveClickListener: ${viewModel.getQueryFilters().printData()}")
    }

    override fun onSortOkClickListener(sort: List<MediaSort>) {
        viewModel.updateQueryFilters(sort)
        Log.d(TAG, "onSortOkClickListener: ${viewModel.getQueryFilters().printData()}")
    }

    override fun getRecycler(): RecyclerView = binding.recycler

    override fun getRecyclerAdapter(): BasePagingAdapter<Media> = mediaAdapter

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(requireContext(), 2)

    override fun getProgressBar(): ProgressBar = binding.progressBar
}