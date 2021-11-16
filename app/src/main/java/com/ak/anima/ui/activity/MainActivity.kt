package com.ak.anima.ui.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.ak.anima.R
import com.ak.anima.ui.base.activity.BaseActivity
import com.ak.anima.databinding.ActivityMainBinding
import com.ak.anima.ui.interfaces.SearchView
import com.ak.anima.ui.interfaces.TabbedView
import com.ak.anima.utils.Keys
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

import android.content.pm.PackageManager
import android.widget.ImageView
import com.ak.anima.ui.SettingsFragment
import com.ak.anima.utils.DataBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    TabbedView, SearchView, SettingsFragment.OnRestartListener {

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

        binding.run {
            navigationView.apply {
                if (viewModel.isAuth()) {
                    val headerView = inflateHeaderView(R.layout.header_nav)
                    setLoginProperties(headerView, menu)
                } else {
                    val headerView = inflateHeaderView(R.layout.header_nav_sign_in)
                    headerView?.findViewById<TextView>(R.id.text_sign_in)?.setOnClickListener {
                        navController.navigate(R.id.action_sign_in)
                    }
                }
                setCheckedItem(navController.graph.startDestination)
                setupWithNavController(navController)
            }

            btnSearch.setOnClickListener { navController.navigate(R.id.nav_search) }

            appBarConfiguration =
                AppBarConfiguration(
                    setOf(
                        R.id.nav_browse_anime,
                        R.id.nav_browse_manga,
                        R.id.nav_discover_media,
                        R.id.nav_trending_media,
                        R.id.nav_myAnime,
                        R.id.nav_myManga
                    ), drawerLayout
                )
            setupActionBarWithNavController(navController, appBarConfiguration)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == navGraph.startDestination)
                    navigationView.setCheckedItem(destination.id)

                //to avoid hiding of tabLayout in case of navigating to login dialog
                if (destination.id != R.id.action_sign_in)
                    appbar.findViewById<TabLayout>(R.id.tabs).apply {
                        when (destination.id) {
                            R.id.nav_trending_media, R.id.nav_discover_media -> {
                                tabMode = TabLayout.MODE_FIXED
                                visibility = View.VISIBLE
                            }
                            R.id.nav_myAnime, R.id.nav_myManga -> {
                                tabMode = TabLayout.MODE_SCROLLABLE
                                visibility = View.VISIBLE
                            }
                            else -> visibility = View.GONE
                        }
                    }
            }
        }
    }


    private fun setLoginProperties(headerView: View?, menu: Menu?) {
        setLoginHeaderView(headerView)
        setNavMenuVisible(menu)
    }

    private fun setLoginHeaderView(headerView: View?) {
        headerView?.run {
            val imageView = findViewById<ImageView>(R.id.user_avatar)
            val username = findViewById<TextView>(R.id.user_name)
            viewModel.authUser?.let {
                DataBinding.setImage(imageView, it.avatar)
                username.text = it.name
            }
        }
    }

    private fun setNavMenuVisible(menu: Menu?) {
        menu?.run {
            val userListSection = menu.findItem(R.id.section_manage)
//            val item = menu.findItem(R.id.nav_home_feed)
            userListSection.isVisible = true
//            item.isVisible = true
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        with(savedInstanceState) {
            val destinationId = getInt(Keys.STATE_NAV_ITEM)
            if (destinationId == R.id.nav_search) {
                val searchQuery = getString(Keys.STATE_SEARCH_QUERY)
                viewModel.performSearch(searchQuery!!)
            }
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        intent?.run {
            val query = getStringExtra(SearchManager.QUERY)
            query?.let {
                viewModel.performSearch(it)
                supportActionBar?.title = query
            }
            val uri = data
            uri?.let { fetchToken(it.fragment) }
        }
        super.onNewIntent(intent)
    }

    private fun fetchToken(fragment: String?) {
        if (fragment == null) {
            Snackbar.make(binding.root,
                "Failed to authenticate! try again later",
                Snackbar.LENGTH_SHORT).show()
            return
        }
        val parametersArr = fragment.split("&")//0 ==> token, 1 ==> header type, 2 ==> expires at
//        parametersArr.forEach { Log.d(TAG, "fetchToken: $it") }

        val token = parametersArr[0].split("=")
        val liveData = viewModel.loadAuthUserData(applicationContext, token[1])
        liveData.observe(this) { authUser ->
            if (authUser != null) {
                restartApp()
                liveData.removeObservers(this)
            }
        }
    }

    override fun restartApp() {
        val packageManager: PackageManager = packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return try {
            navController.navigate(item.itemId)
            true
        } catch (ignore: Exception) {
            super.onOptionsItemSelected(item)
        }
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
            putInt(Keys.STATE_NAV_ITEM, navController.currentDestination!!.id)
            putString(Keys.STATE_SEARCH_QUERY, viewModel.searchQuery.value)
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
}