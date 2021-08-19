package com.ak.otaku_kun.ui.base.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<V, B : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {

    internal lateinit var binding : B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        setSupportActionBar(getToolbar())

        setUpUI()
    }

    abstract fun setUpUI()

    abstract fun getToolbar() : androidx.appcompat.widget.Toolbar
}