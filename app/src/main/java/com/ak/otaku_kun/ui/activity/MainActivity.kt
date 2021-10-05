package com.ak.otaku_kun.ui.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ak.otaku_kun.R
import com.ak.otaku_kun.ui.base.activity.BaseActivity
import com.ak.otaku_kun.databinding.ActivityMainBinding
import com.ak.otaku_kun.ui.interfaces.SearchView
import com.ak.otaku_kun.ui.interfaces.TabbedView
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    TabbedView, SearchView {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun setUpUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = R.id.nav_browse_anime
        navController.graph = navGraph

        appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.nav_browse_anime,
                    R.id.nav_browse_manga,
                    R.id.nav_discover_media,
                    R.id.nav_trending_media
                ), binding.drawerLayout
            )
        setupActionBarWithNavController(navController, appBarConfiguration)
        
        binding.apply {
            navigationView.setCheckedItem(navController.graph.startDestination)
            navigationView.setupWithNavController(navController)
        }

        binding.btnSearch.setOnClickListener { navController.navigate(R.id.nav_search) }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.appbar.findViewById<TabLayout>(R.id.tabs).apply {
                visibility = when (destination.id) {
                    R.id.nav_trending_media, R.id.nav_discover_media -> View.VISIBLE
                    else -> View.GONE
                }
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        with(savedInstanceState) {
            val destinationId = getInt(STATE_NAV_ITEM)
            if (destinationId == R.id.nav_search) {
                val searchQuery = getString(STATE_SEARCH_QUERY)
                viewModel.performSearch(searchQuery!!)
            }
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        val query = intent?.getStringExtra(SearchManager.QUERY)
        query?.let {
            viewModel.performSearch(it)
            supportActionBar?.title = query
        }
        super.onNewIntent(intent)
    }

    override fun onBackPressed() {
        val drawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(STATE_NAV_ITEM, navController.currentDestination!!.id)
            putString(STATE_SEARCH_QUERY, viewModel.searchQuery.value)
        }
        super.onSaveInstanceState(outState)
    }

    override fun getToolbar(): Toolbar = binding.toolbar

    override fun getTabLayout(): TabLayout =
        binding.appbar.findViewById(R.id.tabs)

    override fun showSearchBtn() {
        binding.btnSearch.visibility = View.VISIBLE
    }

    override fun hideSearchBtn() {
        binding.btnSearch.visibility = View.GONE
    }
/*
    private fun onNavigate() {
        when (viewModel.selectedNavItem) {
//            case R.id.nav_home_feed:
//            mFragment = MainFragment.newInstance();
//                changeFragFromDiscover();
//                selectedNavItem = itemID;
//                break;
            R.id.nav_browse_anime -> {
                /* val pagerAdapter =
                     BrowseAnimePagerAdapter(requireContext(), requireActivity(), viewModel.queryFilters)
                 binding.viewPager.adapter = pagerAdapter
                 pagerAdapter.connectTabWitPager(
                     binding.viewPager,
                     binding.appbar.findViewById(R.id.tabs)
                 )*/
//                BrowseAnimeFragment.newInstance(currentDate[1].toInt(), currentDate[0]) //month, year
                //mMenu.setGroupVisible(R.id.browse_menu, true);
                supportActionBar?.setTitle("Anime")
            }
            R.id.nav_browse_manga -> {
                /*val pagerAdapter =
                      BrowseAnimePagerAdapter(requireContext(), requireActivity(), viewModel.queryFilters)
                  binding.viewPager.adapter = pagerAdapter
                  pagerAdapter.connectTabWitPager(
                      binding.viewPager,
                      binding.appbar.findViewById(R.id.tabs)
                  )*/
//                mFragment =
//                    BrowseFragment.newInstance(currentDate[1].toInt(), currentDate[0]) //month, year
                //                mMenu.setGroupVisible(R.id.browse_menu, true);
                supportActionBar?.setTitle("Manga")
            }
            R.id.nav_discover_media -> {
//                mFragment = DiscoverFragment()
                supportActionBar?.setTitle(R.string.discover)
            }
            R.id.nav_trending_media -> {
//                mFragment = MainFragment.newInstance(R.string.trending)
                supportActionBar?.setTitle(R.string.trending)
            }
            /*  case R.id.nav_myanime:
                  mFragment = MainFragment.newInstance();
                  changeFragFromDiscover();
                  selectedNavItem = itemID;
                  break;
              case R.id.nav_mymanga:
                  mFragment = MainFragment.newInstance();
                  changeFragFromDiscover();
                  selectedNavItem = itemID;
                  break;
              case R.id.nav_settings:
                  //TODO: start Activity
                  Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                  break;
              case R.id.nav_about:
                  //TODO: start Activity
                  Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                  break;
              */
        }
    }
*/
    companion object {
        @JvmStatic
        val STATE_NAV_ITEM = "selectedNavItem"

        @JvmStatic
        val STATE_SEARCH_QUERY = "searchQuery"
    }
}