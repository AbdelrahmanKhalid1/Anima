package com.ak.otaku_kun.ui.search

import android.app.SearchManager
import android.content.Context
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentListTabBinding
import com.ak.otaku_kun.ui.activity.MainViewModel
import com.ak.otaku_kun.ui.adapter.pager.SearchPagerAdapter
import com.ak.otaku_kun.ui.base.fragment.BaseFragment
import com.ak.otaku_kun.ui.interfaces.TabbedView
import com.google.android.material.tabs.TabLayout
import com.ak.otaku_kun.ui.interfaces.SearchView as SearchActivity

private const val TAG = "SearchFragment"

class SearchFragment : BaseFragment<FragmentListTabBinding>(R.layout.fragment_list_tab) {
    //TODO make member to know which fragment in viewpager to continue from
    private lateinit var viewModel: MainViewModel
    private lateinit var pagerAdapter: SearchPagerAdapter
    private lateinit var tabbedView: TabbedView
    private lateinit var searchView: SearchActivity
    private lateinit var searchActionView: SearchView
    private var query: String? = null

    override fun setUpUI() {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setHasOptionsMenu(true)
        tabbedView = requireActivity() as TabbedView
        searchView = requireActivity() as SearchActivity
        searchView.hideSearchBtn()
    }

    override fun setObservers() {
        viewModel.searchQuery.observe(viewLifecycleOwner, { query ->
            this.query = query
            query?.let {
                pagerAdapter = SearchPagerAdapter(requireActivity(), query)
                binding.apply {
                    viewPager.adapter = pagerAdapter
                    pagerAdapter.connectTabWithPager(
                        viewPager,
                        tabbedView.getTabLayout().apply { visibility = View.VISIBLE
                        tabMode = TabLayout.MODE_SCROLLABLE})
//                val menu.findItem(R.id.navigation_search).actionView.icons
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        Log.d(TAG, "onCreateOptionsMenu: here")
//        menu.setGroupVisible(R.id.group_main, false)
        val item = menu.findItem(R.id.navigation_search)
        searchActionView = item.actionView as SearchView

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchActionView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        searchActionView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                Log.d("MainActivity", "onQueryTextSubmit: $s")
                return s.isEmpty() //if true will not submit
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

        searchActionView.setOnCloseListener {
            navController.navigateUp()
            query = ""
            false
        }
        if (query == null)
            searchActionView.isIconified = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            searchActionView.clearFocus()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        searchView.showSearchBtn()
        viewModel.resetQuery()
        tabbedView.getTabLayout().apply {tabMode = TabLayout.MODE_FIXED}
        super.onDestroyView()
    }
}