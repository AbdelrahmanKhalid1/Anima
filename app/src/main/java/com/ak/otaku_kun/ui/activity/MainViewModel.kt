package com.ak.otaku_kun.ui.activity

import androidx.lifecycle.*
import com.ak.otaku_kun.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel(){

    var selectedNavItem = R.id.nav_discover

    //private var _dataState = MutableLiveData<PagingData<Anime>>()
    //val dataState : LiveData<PagingData<Anime>> get() = _dataState
    //private val page = MutableLiveData(1)
    //var scrollPosition = 1

//    fun setStateEvent(stateEvent: StateEvent){
//            when(stateEvent){
//                is StateEvent.GetMedias ->{
//                     _dataState = mediaRepository.getBrowseAnime().cachedIn(viewModelScope) as MutableLiveData<PagingData<Anime>>
//                }
//                is StateEvent.None ->{
//
//                }
//            }
//    }

    /**
     * Append new data to current list of data
     */
//    private fun appendData(data : List<Anime>){
//
//    }

//    private fun incrementPage(){
//        page.value = page.value?.plus(1)
//    }

//    fun onChangeScrollPosition(position: Int){
//        scrollPosition = position
//    }
//}

//sealed class StateEvent{
//    object GetMedias : StateEvent()
//    object None : StateEvent()
}