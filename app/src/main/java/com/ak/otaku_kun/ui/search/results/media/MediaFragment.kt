package com.ak.otaku_kun.ui.search.results.media

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
import com.ak.otaku_kun.utils.ItemClickHandler
import com.ak.otaku_kun.utils.Keys
import com.ak.otaku_kun.utils.OnMediaClick
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaFragment : BasePagingListFragment<FragmentListBinding, Media>(R.layout.fragment_list) {

    private lateinit var mediaAdapter: MediaAdapter
    private val viewModel: MediaViewModel by viewModels()

    override fun setUpUI() {
        arguments?.let {
            val mediaType = it.getSerializable(Keys.KEY_MEDIA_TYPE) as MediaType
            val query = it.getString(Keys.KEY_QUERY)
            query?.let {
                viewModel.findMediaByName(mediaType, query)
            }
        }

        val mediaClick = OnMediaClick(parentFragmentManager)
        val mediaClickHandler = ItemClickHandler(requireContext(),mediaClick, mediaClick)
        mediaAdapter = MediaAdapter(true, mediaClickHandler)
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.searchResult.observe(viewLifecycleOwner, { displayData(it) })
    }

    override fun getRecycler(): RecyclerView = binding.recycler

    override fun getRecyclerAdapter(): BasePagingAdapter<Media> = mediaAdapter

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(requireContext(), Const.RECYCLER_GRID_SEARCH_SPAN_COUNT)

    override fun getProgressBar(): View = binding.progressBar
    override fun getErrorView(): View = binding.viewError

    companion object {
        @JvmStatic
        fun newInstance(mediaType: MediaType, query: String): MediaFragment =
            MediaFragment().apply {
                val bundle = Bundle()
                bundle.putSerializable(Keys.KEY_MEDIA_TYPE, mediaType)
                bundle.putString(Keys.KEY_QUERY, query)
                arguments = bundle
            }
    }
}