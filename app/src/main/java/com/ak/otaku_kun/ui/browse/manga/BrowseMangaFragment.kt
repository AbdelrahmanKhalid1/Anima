package com.ak.otaku_kun.ui.browse.manga

import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.model.remote.index.Anime
import com.ak.otaku_kun.ui.adapter.AnimeAdapter
import com.ak.otaku_kun.ui.base.BaseAdapter
import com.ak.otaku_kun.ui.base.BaseListFragment

class BrowseMangaFragment : BaseListFragment<BrowseMangaViewModel, FragmentListBinding, Anime>(R.layout.fragment_list) {

    private lateinit var animeAdapter: AnimeAdapter

    override fun setUpUI() {
        animeAdapter = AnimeAdapter()
        super.setUpUI()
    }

    override fun setObservers() {
    }

    override fun getRecycler(): RecyclerView = binding.recycler

    override fun getRecyclerAdapter(): BaseAdapter<Anime> = animeAdapter

    override fun getProgressBar(): ProgressBar = binding.progressBar

    companion object{
        @JvmStatic
        fun newInstance(){

        }
    }
}