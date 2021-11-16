package com.ak.anima.ui.trending

import android.os.Bundle
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
import com.ak.anima.utils.Const
import com.ak.anima.utils.ItemClickHandler
import com.ak.anima.utils.Keys
import com.ak.anima.utils.OnMediaClick
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

        val mediaClick = OnMediaClick(parentFragmentManager)
        val mediaClickHandler = ItemClickHandler(requireContext(),mediaClick, mediaClick)
        mediaAdapter = MediaAdapter(mediaClickHandler = mediaClickHandler)
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.data.observe(viewLifecycleOwner, { displayData(it) })
    }

    override fun getRecycler(): RecyclerView = binding.recycler

    override fun getRecyclerAdapter(): BasePagingAdapter<Media> = mediaAdapter

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager =
        GridLayoutManager(requireContext(), Const.RECYCLER_GRID_SPAN_COUNT)

    override fun getProgressBar(): View = binding.customViewLoad
    override fun getErrorView(): CustomViewErrorBinding = binding.viewError

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