package com.ak.otaku_kun.ui.details.media.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.AdapterEntityGroupBinding
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.databinding.ItemCharacterBinding
import com.ak.otaku_kun.model.index.Character
import com.ak.otaku_kun.ui.adapter.recycler.CharacterAdapter.CharacterViewHolder
import com.ak.otaku_kun.ui.base.adapter.BaseAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder
import com.ak.otaku_kun.ui.base.fragment.BaseListFragment
import com.ak.otaku_kun.ui.details.media.MediaViewModel
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.DataState
import com.ak.otaku_kun.utils.Keys
import com.ak.otaku_kun.utils.StateEvent

private const val TAG = "MediaCharacterFragment"

class MediaCharacterFragment :
    BaseListFragment<FragmentListBinding, Character>(R.layout.fragment_list) {

    private val viewModel: MediaViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var characterAdapter: GroupedCharacterAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

    override fun setUpUI() {
        isPager = true
        //init recycler arguments
        characterAdapter = GroupedCharacterAdapter()
        layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun setObservers() {
        viewModel.mediaLiveData.observe(viewLifecycleOwner) { media ->
            Log.d(TAG, "setObservers: ${media.id}")
            media.run {
                if (viewModel.mediaCharactersLiveData.value == null) {
                    viewModel.triggerStateEvent(StateEvent.LoadMediaCharacters)
                }
            }
        }
        viewModel.mediaCharactersLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> displayData(it.data as List<Character>)
                is DataState.Error -> handleError(LoadState.Error(it.exception))
                else -> {}
            }
        }
    }

    override fun loadMoreItems() {
        viewModel.triggerStateEvent(StateEvent.LoadMediaCharacters)
    }

    override fun getProgressBar(): View = binding.progressBar
    override fun getErrorView(): View = binding.viewError
    override fun getRecycler(): RecyclerView = binding.recycler
    override fun getRecyclerAdapter(): BaseAdapter<Character> = characterAdapter
    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = layoutManager
    override fun getRecyclerFirstVisibleItem(): Int {
        val firstPositions = layoutManager.findFirstVisibleItemPositions(null)
        if (firstPositions?.isNotEmpty() == true)
            return firstPositions[0]
        return 0
    }
}

class GroupedCharacterAdapter : BaseAdapter<Character>() {

    private var firstNewHeaderPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<Character> {
        return if (viewType == Keys.RECYCLER_TYPE_CONTENT) {
            val binding =
                ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CharacterViewHolder(binding)
        } else {
            val binding = AdapterEntityGroupBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
            HeaderViewHolder(binding)
        }
//        val mediaClick = OnMediaClick()
//        val mediaClickHandler = ItemClickHandler(binding.root.context, mediaClick)
//        val mediaAdapter = MediaRelationAdapter.MediaAdapter(mediaClickHandler)
    }

    override fun addItems(items: List<Character>) {
        if (firstNewHeaderPosition != Const.NO_ITEM &&
            items[0].role == this.items[firstNewHeaderPosition].role
        ) { //first item is always header so check if not new category remove it
            super.addItems(items.subList(1, items.size))
        } else super.addItems(items)
    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG, "getItemViewType: $position")
        return if (items[position].id == Keys.RECYCLER_TYPE_HEADER) {
            firstNewHeaderPosition = position
            Keys.RECYCLER_TYPE_HEADER
        } else Keys.RECYCLER_TYPE_CONTENT
    }

    class HeaderViewHolder(private val binding: AdapterEntityGroupBinding) :
        BaseViewHolder<Character>(binding.root) {
        override fun bind(item: Character) {
            binding.apply {
                title = item.role
            }
        }
    }
}