package com.ak.anima.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.ak.anima.R
import com.ak.anima.databinding.FragmentListTabBinding
import com.ak.anima.ui.activity.MainViewModel
import com.ak.anima.ui.adapter.pager.SearchPagerAdapter
import com.ak.anima.ui.base.fragment.BaseFragment
import com.ak.anima.ui.interfaces.TabbedView
import com.ak.anima.utils.Keys
import com.google.android.material.tabs.TabLayout
import com.ak.anima.ui.interfaces.SearchView as SearchActivity

private const val TAG = "SearchFragment"

class SearchFragment : BaseFragment<FragmentListTabBinding>(R.layout.fragment_list_tab) {
    //TODO make member to know which fragment in viewpager to continue from
    private lateinit var viewModel: MainViewModel
    private lateinit var pagerAdapter: SearchPagerAdapter
    private lateinit var tabbedActivity: TabbedView
    private lateinit var searchActivity: SearchActivity
    private lateinit var searchView: SearchView
    private var selectedViewPagerItem: Int = 0
    private var query: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        savedInstanceState?.run {
            selectedViewPagerItem = getInt(Keys.STATE_VIEW_PAGER_POSITION, 0)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setUpUI() {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setHasOptionsMenu(true)
        tabbedActivity = requireActivity() as TabbedView
        searchActivity = requireActivity() as SearchActivity
        searchActivity.hideSearchBtn()
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
                        tabbedActivity.getTabLayout().apply {
                            visibility = View.VISIBLE
                            tabMode = TabLayout.MODE_SCROLLABLE
                        })

                    //process killed reset
                    if(selectedViewPagerItem != 0)
                    viewPager.setCurrentItem(selectedViewPagerItem, false)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        Log.d(TAG, "onCreateOptionsMenu: here")
//        menu.setGroupVisible(R.id.group_main, false)
        val item = menu.findItem(R.id.navigation_search)
        searchView = item.actionView as SearchView

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                Log.d("MainActivity", "onQueryTextSubmit: $s")
                return s.isEmpty() //if true will not submit
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener {
            navController.navigateUp()
            query = ""
            false
        }

        if (query == null) {//button search was clicked
            searchView.isIconified = false
        }
        else {//process killed or in case reloading fragment when connecting tabs with pager
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.title = query
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            searchView.clearFocus()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        searchActivity.showSearchBtn()
        viewModel.resetQuery()
        tabbedActivity.getTabLayout().apply { tabMode = TabLayout.MODE_FIXED }
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply{
            putInt(Keys.STATE_VIEW_PAGER_POSITION, binding.viewPager.currentItem)
        }
        super.onSaveInstanceState(outState)
    }
}