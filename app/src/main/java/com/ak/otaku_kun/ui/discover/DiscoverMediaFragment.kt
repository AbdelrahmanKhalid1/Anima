package com.ak.otaku_kun.ui.discover

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.adapter.recycler.DiscoverAdapter
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.fragment.BaseListFragment
import com.ak.otaku_kun.utils.*
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DiscoverMediaFragment"

@AndroidEntryPoint
class DiscoverMediaFragment : BaseListFragment<FragmentListBinding, List<Media>>(R.layout.fragment_list) {

    //TODO handle horizontal orientation
    private val viewModel : DiscoverViewModel by viewModels()
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var discoverAdapter : DiscoverAdapter
    private lateinit var mediaType: MediaType

    override fun setUpUI() {
        arguments?.let {
            mediaType = it.get(Keys.KEY_MEDIA_TYPE) as MediaType
            if(viewModel.mediaLiveData.value == null)
                viewModel.getDiscoverMediaData(mediaType)
        }
        //init recycler arguments
        val mediaClickBehavior = OnMediaClick()
        val mediaClickHandler = ItemClickHandler(requireContext(), mediaClickBehavior)
        discoverAdapter = DiscoverAdapter(mediaType.rawValue, mediaClickHandler)
        layoutManager = LinearLayoutManager(requireContext())

        getErrorView().findViewById<Button>(R.id.btn_error).setOnClickListener {
            viewModel.getDiscoverMediaData(mediaType)
        }
    }

    override fun setObservers() {
        viewModel.mediaLiveData.observe(viewLifecycleOwner, { dataState ->
            when(dataState){
                is DataState.Loading -> {
                    getProgressBar().visibility = View.VISIBLE
                    getErrorView().visibility = View.GONE
                }
                is DataState.Success -> displayData(dataState.data)
                is DataState.Error -> handleError(LoadState.Error(dataState.exception))
            }})
    }
    override fun loadMoreItems() {}

    override fun getRecycler(): RecyclerView  = binding.recycler
    override fun getRecyclerAdapter(): BaseAdapter<List<Media>> = discoverAdapter
    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = layoutManager
    override fun getRecyclerFirstVisibleItem(): Int = layoutManager.findLastCompletelyVisibleItemPosition()
    override fun getProgressBar(): View = binding.progressBar
    override fun getErrorView(): View = binding.viewError

    companion object{
        @JvmStatic
        fun newInstance(mediaType: MediaType): DiscoverMediaFragment =
            DiscoverMediaFragment().apply {
                arguments = Bundle().apply {
                    Log.d(TAG, "newInstance: $mediaType")
                    putSerializable(Keys.KEY_MEDIA_TYPE, mediaType)
                }
            }
    }
}