package com.ak.otaku_kun.ui.browse

import android.annotation.SuppressLint
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.FragmentMainBinding
import com.ak.otaku_kun.ui.*
import com.ak.otaku_kun.ui.adapter.BrowsePagerAdapter
import com.ak.otaku_kun.ui.base.BaseFragment
import com.ak.otaku_kun.ui.dialog.FilterQueryDialog
import com.ak.otaku_kun.ui.dialog.SortDialog
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.type.MediaSort
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "MainFragment"

@AndroidEntryPoint
class MainFragment :
    BaseFragment<MainFragmentViewModel, FragmentMainBinding>(R.layout.fragment_main),
    NavigationView.OnNavigationItemSelectedListener, SortDialog.SortDialogListener,
    FilterQueryDialog.OnFilterSaveClickListener {

    private val viewModel: MainFragmentViewModel by viewModels()

    override fun setUpUI() {
        val toolbar = binding.appbar.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            requireActivity(), binding.drawerLayout, toolbar,
            R.string.drawer_open, R.string.drawer_close
        )

        binding.apply {
            drawerLayout.addDrawerListener(toggle)
            navigationView.setNavigationItemSelectedListener(this@MainFragment)
            navigationView.setCheckedItem(viewModel.selectedNavItem)
        }
        toggle.syncState()

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Fragment back pressed invoked")
                    // Do custom work here
                    val drawerLayout = binding.drawerLayout
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                        return
                    }

                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
            )

////        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApolloHelper.API_AUTH_LINK + "?client_id=" + "3265" +"&response_type=token"));
////        startActivity(intent);


////        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApolloHelper.API_AUTH_LINK + "?client_id=" + "3265" +"&response_type=token"));
////        startActivity(intent);
//        if (savedInstanceState == null) {
//            mFragment = DiscoverFragment()
//            supportFragmentManager.beginTransaction().replace(
//                R.id.frag_container,
//                mFragment
//            ).commit()
//            mSelectedNavItem = R.id.nav_discover
//        }

//        binding.btnSearch.setOnClickListener { v ->
//            startActivity(
//                Intent(
//                    applicationContext,
//                    SearchActivity::class.java
//                )
//            )
//        }
//        setNavMenuVisible()
    }

    /*private fun setNavMenuVisible() {
        val menu = binding.navigationView.menu
        val manageItems = menu.findItem(R.id.action_manage)
        val homeItem = menu.findItem(R.id.nav_home_feed)
        manageItems.isVisible = true
        homeItem.isVisible = true
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemID = item.itemId
        Log.d(TAG, "onNavigationItemSelected: ${viewModel.selectedNavItem} $itemID")
        if(viewModel.selectedNavItem != itemID){
            viewModel.selectedNavItem = itemID
            onNavigate()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun onNavigate() {
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        when (viewModel.selectedNavItem) {
//            case R.id.nav_home_feed:
//            mFragment = MainFragment.newInstance();
//                changeFragFromDiscover();
//                selectedNavItem = itemID;
//                break;
            R.id.nav_browse_anime -> {
                val pagerAdapter =
                    BrowsePagerAdapter(requireContext(), requireActivity(), viewModel.queryFilters)
                binding.viewPager.adapter = pagerAdapter
                pagerAdapter.connectTabWitPager(
                    binding.viewPager,
                    binding.appbar.findViewById(R.id.tabs)
                )
//                BrowseAnimeFragment.newInstance(currentDate[1].toInt(), currentDate[0]) //month, year
                //mMenu.setGroupVisible(R.id.browse_menu, true);
                supportActionBar?.setTitle("Anime")
            }
            R.id.nav_browse_manga -> {
                navController.popBackStack()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    @SuppressLint("RestrictedApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //TODO maybe can hide menu in discover navigate
            R.id.action_filter -> {
                val filterQueryDialog =
                    FilterQueryDialog(viewModel.queryFilters, viewModel.selectedNavItem, this)
                filterQueryDialog.show(parentFragmentManager, "FilterQueryDialog")
                true
            }
            R.id.action_sort -> {
                Log.d(TAG, "onResume: ${viewModel.queryFilters.printData()}")
                val sortDialog = SortDialog(viewModel.queryFilters.listSort!![0].rawValue, this)
                sortDialog.show(parentFragmentManager, "GenreDialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveClickListener(queryFilters: QueryFilters) {
        viewModel.queryFilters = queryFilters
        binding.navigationView.setCheckedItem(viewModel.getNewSelectedNavAfterFilter())
        onNavigate()
    }

    override fun onSortOkClickListener(sort: List<MediaSort>) {
        viewModel.queryFilters.listSort = sort
        onNavigate()
    }

    override fun setObservers() {}
}