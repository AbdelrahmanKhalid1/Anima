package com.ak.otaku_kun.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint

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