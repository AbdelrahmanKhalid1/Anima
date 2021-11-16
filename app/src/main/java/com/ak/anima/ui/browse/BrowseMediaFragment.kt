package com.ak.anima.ui.browse

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.anima.R
import com.ak.anima.databinding.CustomViewErrorBinding
import com.ak.anima.databinding.FragmentListBinding
import com.ak.anima.model.index.Media
import com.ak.anima.ui.adapter.recycler.MediaAdapter
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.ak.anima.ui.base.fragment.BasePagingListFragment
import com.ak.anima.ui.dialog.FilterQueryDialog
import com.ak.anima.ui.dialog.SortDialog
import com.ak.anima.utils.*
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
            R.id.action_filter -> {
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
        viewModel.setQueryFilters(queryFilters)
    }

    override fun onSortOkClickListener(sort: List<MediaSort>) {
        viewModel.updateQueryFilters(sort)
    }

    override fun getRecycler(): RecyclerView = binding.recycler
    override fun getRecyclerAdapter(): BasePagingAdapter<Media> = mediaAdapter
    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(requireContext(), Const.RECYCLER_GRID_SPAN_COUNT)

    override fun getProgressBar(): View = binding.customViewLoad
    override fun getErrorView(): CustomViewErrorBinding = binding.viewError
}