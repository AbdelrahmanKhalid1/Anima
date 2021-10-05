package com.ak.otaku_kun.ui.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<B : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {

    internal lateinit var binding : B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        setSupportActionBar(getToolbar())

        setUpUI()
    }

    fun disableToolbarTitle() {
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    abstract fun setUpUI()
    abstract fun getToolbar() : androidx.appcompat.widget.Toolbar
}