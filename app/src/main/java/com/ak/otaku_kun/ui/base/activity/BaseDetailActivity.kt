package com.ak.otaku_kun.ui.base.activity

import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.Keys
import com.ogaclejapan.smarttablayout.SmartTabLayout
import kotlin.properties.Delegates

private const val TAG = "BaseDetailActivity"
abstract class BaseDetailActivity<B : ViewDataBinding>(layoutId: Int) : BaseActivity<B>(layoutId) {

    internal var itemId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) itemId =
            savedInstanceState.getInt(Keys.KEY_ITEM_ID, Const.NO_ITEM)
        else
            intent?.let {
                itemId = intent.getIntExtra(Keys.KEY_ITEM_ID, Const.NO_ITEM)
            }
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setUpUI() {
        if (itemId != Const.NO_ITEM) {
            getViewPager().adapter = getTabAdapter()
            getViewPager().offscreenPageLimit = Const.MAX_PAGE_OFF_SCREEN_LIMIT
            getTabLayout().setViewPager(getViewPager())
        }else{
            //todo handle error
        }
        setObservers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putInt(Keys.KEY_ITEM_ID, itemId)
        }
        super.onSaveInstanceState(outState)
    }

    abstract fun setObservers()
    abstract fun getTabAdapter(): PagerAdapter?
    abstract fun getViewPager(): ViewPager
    abstract fun getTabLayout(): SmartTabLayout

    override fun onDestroy() {
        getViewPager().adapter = null
        super.onDestroy()
    }
}