package com.ak.anima.ui.search.results.character

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.anima.R
import com.ak.anima.databinding.CustomViewErrorBinding
import com.ak.anima.databinding.FragmentListBinding
import com.ak.anima.model.index.Character
import com.ak.anima.ui.adapter.recycler.CharacterAdapter
import com.ak.anima.ui.base.adapter.BasePagingAdapter
import com.ak.anima.ui.base.fragment.BasePagingListFragment
import com.ak.anima.utils.Const
import com.ak.anima.utils.Keys
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment : BasePagingListFragment<FragmentListBinding, Character>(R.layout.fragment_list) {

    private lateinit var characterAdapter : CharacterAdapter
    private val viewModel: CharacterViewModel by viewModels()

    override fun setUpUI() {
        arguments?.let {
            val query = it.getString(Keys.KEY_QUERY)
            query?.let {
                if (viewModel.searchResult.value == null)
                viewModel.getCharactersByName(query)
            }
        }

        characterAdapter = CharacterAdapter()
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.searchResult.observe(viewLifecycleOwner, {displayData(it)})
    }

    override fun getRecycler(): RecyclerView  = binding.recycler

    override fun getRecyclerAdapter(): BasePagingAdapter<Character> = characterAdapter

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(requireContext(), Const.RECYCLER_GRID_SEARCH_SPAN_COUNT)

    override fun getProgressBar(): View = binding.customViewLoad

    override fun getErrorView(): CustomViewErrorBinding = binding.viewError

    companion object{
        @JvmStatic
        fun newInstance(query: String): CharacterFragment = CharacterFragment().apply {
            val args = Bundle()
            args.putString(Keys.KEY_QUERY, query)
            arguments = args
        }
    }
}
