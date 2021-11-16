package com.ak.anima.ui.user_list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.anima.R
import com.ak.anima.databinding.CustomViewErrorBinding
import com.ak.anima.databinding.FragmentListBinding
import com.ak.anima.databinding.FragmentListTabBinding
import com.ak.anima.model.index.Media
import com.ak.anima.ui.adapter.pager.UserMediaListPagerAdapter
import com.ak.anima.ui.adapter.recycler.MediaAdapter
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.ak.anima.ui.base.fragment.BaseFragment
import com.ak.anima.ui.base.fragment.BasePagingListFragment
import com.ak.anima.ui.browse.MEDIA_TYPE
import com.ak.anima.ui.interfaces.TabbedView
import com.ak.anima.utils.ItemClickHandler
import com.ak.anima.utils.Keys
import com.ak.anima.utils.OnMediaClick
import com.ak.anima.utils.StateEvent
import com.ak.type.MediaListStatus
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserMediaFragment : BaseFragment<FragmentListTabBinding>(R.layout.fragment_list_tab) {

    private lateinit var pagerAdapter: UserMediaListPagerAdapter
    private lateinit var tabbedView: TabbedView
    private var viewPagerPosition = 0
    private var mediaType: MediaType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            mediaType = get(MEDIA_TYPE) as MediaType?
        }
        savedInstanceState?.run {
            viewPagerPosition = getInt(Keys.STATE_VIEW_PAGER_POSITION, 0)
        }
    }

    override fun setUpUI() {
        tabbedView = requireActivity() as TabbedView
        pagerAdapter =
            UserMediaListPagerAdapter(requireActivity(), mediaType ?: MediaType.UNKNOWN__)
        binding.viewPager.apply {
            adapter = pagerAdapter
            pagerAdapter.connectTabWithPager(this, tabbedView.getTabLayout())
            if (viewPagerPosition != 0)
                setCurrentItem(viewPagerPosition, false)
        }
    }

    override fun setObservers() {}

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putInt(Keys.STATE_VIEW_PAGER_POSITION, binding.viewPager.currentItem)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}

@AndroidEntryPoint
class UserMediaListFragment :
    BasePagingListFragment<FragmentListBinding, Media>(R.layout.fragment_list) {

    private val viewModel: UserMediaListViewModel by viewModels()
    private lateinit var mediaAdapter: MediaAdapter
    private lateinit var layoutManger: RecyclerView.LayoutManager
    private var mediaType: MediaType? = null
    private var mediaListStatus: MediaListStatus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mediaType = it.get(Keys.KEY_MEDIA_TYPE) as MediaType
            mediaListStatus = it.get(Keys.KEY_MEDIA_LIST_STATUS) as MediaListStatus
        }
    }

    override fun setUpUI() {
        val itemClickHandler = ItemClickHandler(requireContext(), OnMediaClick())
        mediaAdapter = MediaAdapter(mediaClickHandler = itemClickHandler, isMyList = true)
        layoutManger = GridLayoutManager(requireContext(), 2)
        viewModel.onTriggerStateEvent(StateEvent.LoadData, mediaType, mediaListStatus)
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.mediaList.observe(viewLifecycleOwner) { displayData(it) }
    }

    override fun getRecycler(): RecyclerView = binding.recycler
    override fun getRecyclerAdapter(): BasePagingAdapter<Media> = mediaAdapter
    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = layoutManger
    override fun getProgressBar(): View = binding.customViewLoad
    override fun getErrorView(): CustomViewErrorBinding = binding.viewError

    companion object {
        @JvmStatic
        fun newInstance(
            mediaType: MediaType,
            mediaListStatus: MediaListStatus,
        ): UserMediaListFragment =
            UserMediaListFragment().apply {
                arguments = bundleOf(
                    Pair(Keys.KEY_MEDIA_TYPE, mediaType),
                    Pair(Keys.KEY_MEDIA_LIST_STATUS, mediaListStatus)
                )
            }
    }
}