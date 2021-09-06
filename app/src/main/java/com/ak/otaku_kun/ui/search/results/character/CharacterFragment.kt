package com.ak.otaku_kun.ui.search.results.character

import android.os.Bundle
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.index.Character
import com.ak.otaku_kun.ui.adapter.recycler.CharacterAdapter
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.fragment.BasePagingListFragment
import com.ak.otaku_kun.utils.Const
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment : BasePagingListFragment<FragmentListBinding, Character>(R.layout.fragment_list) {

    private lateinit var characterAdapter : CharacterAdapter
    private val viewModel: CharacterViewModel by viewModels()

    override fun setUpUI() {
        arguments?.let {
            val query = it.getString(Const.KEY_QUERY)
            query?.let {
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

    override fun getProgressBar(): ProgressBar = binding.progressBar

    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 3)

    companion object{
        @JvmStatic
        fun newInstance(query: String): CharacterFragment = CharacterFragment().apply {
            val args = Bundle()
            args.putString(Const.KEY_QUERY, query)
            arguments = args
        }
    }
}
