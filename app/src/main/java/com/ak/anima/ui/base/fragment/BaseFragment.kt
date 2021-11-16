package com.ak.anima.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

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
        try {
            navController = findNavController()
        } catch (ignore: Exception) {
            
        }
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