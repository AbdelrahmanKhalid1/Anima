package com.ak.otaku_kun.ui.activity

import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ak.otaku_kun.R
import com.ak.otaku_kun.ui.base.BaseActivity
import com.ak.otaku_kun.databinding.ActivityMainBinding
import com.ak.otaku_kun.ui.browse.BrowseMediaFragment
import com.ak.otaku_kun.ui.dialog.FilterQueryDialog
import com.ak.otaku_kun.ui.dialog.SortDialog
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.type.MediaSort
import com.ak.type.MediaType
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_appbar.view.*

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main),
    SortDialog.SortDialogListener, FilterQueryDialog.OnFilterSaveClickListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun setUpUI() {

        /*  val toggle = ActionBarDrawerToggle(
              this, binding.drawerLayout, getToolbar(),
              R.string.drawer_open, R.string.drawer_close
          )
  */
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.nav_browse_anime, R.id.nav_browse_manga), binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.apply {
            navigationView.setCheckedItem(navController.graph.startDestination)
            navigationView.setupWithNavController(navController)
        }
        //toggle.syncState()
    }

    override fun onBackPressed() {
        val drawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

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
            R.id.nav_discover -> {
//                mFragment = DiscoverFragment()
                supportActionBar?.setTitle(R.string.discover)
            }
            R.id.nav_trending -> {
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

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //TODO maybe can hide menu in discover navigate
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val filterQueryDialog =
                    FilterQueryDialog(viewModel.queryFilters, viewModel.selectedNavItem, this)
                filterQueryDialog.show(supportFragmentManager, "FilterQueryDialog")
                true
            }
            R.id.action_sort -> {
                Log.d(TAG, "onResume: ${viewModel.queryFilters.printData()}")
                val sortDialog = SortDialog(viewModel.queryFilters.listSort!![0].rawValue, this)
                sortDialog.show(supportFragmentManager, "GenreDialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/

    override fun onSaveClickListener(queryFilters: QueryFilters) {
        viewModel.queryFilters = queryFilters
        binding.navigationView.setCheckedItem(viewModel.getNewSelectedNavAfterFilter())
        onNavigate()
    }

    override fun onSortOkClickListener(sort: List<MediaSort>) {
        viewModel.queryFilters.listSort = sort
        onNavigate()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun getToolbar(): Toolbar = binding.appbar as Toolbar
}