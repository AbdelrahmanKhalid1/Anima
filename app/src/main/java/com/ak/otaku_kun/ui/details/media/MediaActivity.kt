package com.ak.otaku_kun.ui.details.media

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.ActivityMediaBinding
import com.ak.otaku_kun.ui.adapter.pager.MediaPagerAdapter
import com.ak.otaku_kun.ui.base.activity.BaseDetailActivity
import com.ak.otaku_kun.utils.DataBinding
import com.ogaclejapan.smarttablayout.SmartTabLayout
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MediaActivity"
@AndroidEntryPoint
class MediaActivity : BaseDetailActivity<ActivityMediaBinding>(R.layout.activity_media) {

    private val viewModel: MediaViewModel by viewModels()
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.run {
        }
    }

    override fun setUpUI() {
        super.setUpUI()
        disableToolbarTitle()
        binding.collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT)
        viewModel.setMediaId(itemId)
    }

    override fun setObservers() {
        viewModel.mediaLiveData.observe(this, { media ->
            media?.run {
                binding.media = this
                binding.collapsingToolbar.title = title.userPreferred
                DataBinding.setImage(binding.customCover.findViewById(R.id.media_cover), cover)
               /* media.relations.forEach {
                    Log.d(TAG, "setObservers: relation = ${it.key}, mediaList = ${it.value.size} ${it.value.map { media-> "${media.title} " }}")
                }*/
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val isAuth: Boolean = getPresenter().getSettings().isAuthenticated()
        menuInflater.inflate(R.menu.media_menu, menu)
        this.menu = menu
        //TODO set icons for favourite and manage
//        setManageMenuItemIcon()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (model != null) {
        when (item.itemId) {
            R.id.action_favourite -> {

            }
            R.id.action_manage -> {
//                mediaActionUtil = Builder()
//                    .setId(model.getId()).build(this)
//                mediaActionUtil.startSeriesAction()
            }
            R.id.action_share -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
//                intent.putExtra(
//                    Intent.EXTRA_TEXT, String.format(
//                        Locale.getDefault(),
//                        "%s - %s", model.getTitle().getUserPreferred(), model.getSiteUrl()
//                    )
//                )
                intent.type = "text/plain"
                startActivity(intent)
            }
        }
//        } else NotifyUtil.INSTANCE.makeText(
//            applicationContext,
//            R.string.text_activity_loading,
//            Toast.LENGTH_SHORT
//        ).show()
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
        }
        super.onSaveInstanceState(outState)
    }

    override fun getToolbar(): Toolbar = binding.toolbar
    override fun getTabAdapter(): PagerAdapter =
        MediaPagerAdapter(supportFragmentManager, this, itemId)

    override fun getViewPager(): ViewPager = binding.viewPager
    override fun getTabLayout(): SmartTabLayout = binding.tabs

    override fun onDestroy() {
        viewModel.cancelJob()
        super.onDestroy()
    }
}