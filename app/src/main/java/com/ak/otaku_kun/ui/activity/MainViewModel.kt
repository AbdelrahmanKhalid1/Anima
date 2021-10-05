package com.ak.otaku_kun.ui.activity

import androidx.lifecycle.*
import com.ak.otaku_kun.R
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel(){

    private val _searchQuery = MutableLiveData<String?>()
    val searchQuery : LiveData<String?> get() = _searchQuery

    fun performSearch(query: String){
        _searchQuery.value = query
    }

    fun resetQuery(){
        _searchQuery.value = null
    }
}
