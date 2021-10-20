package com.ak.otaku_kun.ui.details.media.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.adapter.recycler.MediaRelationAdapter
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.fragment.BaseListFragment
import com.ak.otaku_kun.ui.details.media.MediaViewModel

class MediaRelationFragment :
    BaseListFragment<FragmentListBinding, Pair<String, List<Media>>>(R.layout.fragment_list) {

    private val viewModel: MediaViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var relationAdapter: MediaRelationAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun setUpUI() {
        //init recycler arguments
        relationAdapter = MediaRelationAdapter()
        layoutManager = LinearLayoutManager(requireContext())
    }

    override fun setObservers() {
        viewModel.mediaLiveData.observe(viewLifecycleOwner) {
            it?.let { media ->
                relationAdapter.setItems(media.relations)
            }
        }
    }

    override fun loadMoreItems() {}

    override fun getRecycler(): RecyclerView = binding.recycler
    override fun getRecyclerAdapter(): BaseAdapter<Pair<String, List<Media>>> = relationAdapter
    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = layoutManager
    override fun getRecyclerFirstVisibleItem(): Int =
        layoutManager.findLastCompletelyVisibleItemPosition()

    override fun getProgressBar(): View = binding.progressBar
    override fun getErrorView(): View = binding.viewError
}