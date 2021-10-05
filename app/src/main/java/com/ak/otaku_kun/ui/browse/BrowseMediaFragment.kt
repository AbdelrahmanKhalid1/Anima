package com.ak.otaku_kun.ui.browse

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
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
import com.ak.otaku_kun.utils.*
import com.ak.type.MediaSort
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint
import kotlin.NullPointerException

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
                if (viewModel.queryFilters.value == null)
                    viewModel.onTriggerStateEvent(StateEvent.InitQueryFilters(mediaType))
            }
        }

        val mediaClick = OnMediaClick(parentFragmentManager)
        val mediaClickHandler = ItemClickHandler(requireContext(),mediaClick, mediaClick)
        mediaAdapter = MediaAdapter(mediaClickHandler = mediaClickHandler)
        super.setUpUI()
        //getRecycler().scrollToPosition(viewModel.scrollPosition)
    }

    override fun setObservers() {
        viewModel.queryFilters.observe(
            viewLifecycleOwner,
            {
                setUpUI()
                viewModel.onTriggerStateEvent(StateEvent.LoadData)
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
                filterQueryDialog.show(parentFragmentManager, "FilterQueryDialog")
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
//        Log.d(TAG, "onFilter before: ${viewModel.getQueryFilters().printData()}")
        viewModel.setQueryFilters(queryFilters)
//        Log.d(TAG, "onFilter after: ${viewModel.getQueryFilters().printData()}")
    }

    override fun onSortOkClickListener(sort: List<MediaSort>) {
//        Log.d(TAG, "onSort Before: ${viewModel.getQueryFilters().printData()}")
        viewModel.updateQueryFilters(sort)
//        Log.d(TAG, "onSort After: ${viewModel.getQueryFilters().printData()}")
    }

    override fun getRecycler(): RecyclerView = binding.recycler
    override fun getRecyclerAdapter(): BasePagingAdapter<Media> = mediaAdapter
    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(requireContext(), Const.RECYCLER_GRID_SPAN_COUNT)

    override fun getProgressBar(): View = binding.progressBar
    override fun getErrorView(): View = binding.viewError
}