package com.ak.otaku_kun.ui.base.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseFragment<V : ViewDataBinding>(private val layoutId: Int) :
    Fragment() {

    lateinit var navController: NavController
    private var _binding: V? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (savedInstanceState != null)
//            _binding = DataBindingUtil.bind(view)
            navController = findNavController()
//        try {
//        } catch (ignore: Exception) {
//            Log.e(TAG, "onViewCreated: Fragment Does not have NavController", ignore)
//            navController = findNavController()
//        }
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        setUpUI()
        setObservers()
    }

    abstract fun setUpUI()

    abstract fun setObservers()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}