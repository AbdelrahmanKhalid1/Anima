package com.ak.otaku_kun.ui.discover

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.ui.adapter.recycler.DiscoverAdapter
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.fragment.BaseListFragment
import com.ak.otaku_kun.utils.Const
import com.ak.type.MediaType
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DiscoverMediaFragment"

@AndroidEntryPoint
class DiscoverMediaFragment : BaseListFragment<FragmentListBinding, List<Media>>(R.layout.fragment_list) {

    //TODO handle horizontal orientation
    private val viewModel : DiscoverViewModel by viewModels()
    private lateinit var discoverAdapter : DiscoverAdapter
    private lateinit var mediaType: MediaType
    override fun setUpUI() {
        arguments?.let {
            mediaType = it.get(Const.KEY_MEDIA_TYPE) as MediaType
            dataHandler.displayProgressBar(getProgressBar())
            if(viewModel.data.value == null)
            viewModel.getDiscoverMediaData(mediaType)
        }
        discoverAdapter = DiscoverAdapter(mediaType.rawValue)
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.data.observe(viewLifecycleOwner, {discover -> displayData(discover)})
    }

    override fun getRecycler(): RecyclerView  = binding.recycler

    override fun getRecyclerAdapter(): BaseAdapter<List<Media>> = discoverAdapter

    override fun getProgressBar(): ProgressBar = binding.progressBar
    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

    companion object{
        @JvmStatic
        fun newInstance(mediaType: MediaType): DiscoverMediaFragment =
            DiscoverMediaFragment().apply {
                arguments = Bundle().apply {
                    Log.d(TAG, "newInstance: $mediaType")
                    putSerializable(Const.KEY_MEDIA_TYPE, mediaType)
                }
            }
    }
}