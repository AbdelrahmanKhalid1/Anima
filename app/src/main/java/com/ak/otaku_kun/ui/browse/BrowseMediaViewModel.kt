package com.ak.otaku_kun.ui.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.otaku_kun.model.remote.index.Media
import com.ak.otaku_kun.repository.MediaRepository
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.otaku_kun.utils.StateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseMediaViewModel @Inject constructor(private val mediaRepository: MediaRepository,) :
    ViewModel() {
    var scrollPosition = 0
    private var _dataState = MutableLiveData<PagingData<Media>>()
    val dataState: LiveData<PagingData<Media>> get() = _dataState
    //TODO make member to know which fragment in viewpager to continue from
    //private val page = MutableLiveData(1)

    fun onTriggerStateEvent(stateEvent: StateEvent, filters: QueryFilters) {
        viewModelScope.launch {
            when (stateEvent) {
                is StateEvent.LoadAnime -> {
                    _dataState = mediaRepository.getBrowseAnime(filters)
                        .cachedIn(viewModelScope) as MutableLiveData<PagingData<Media>>
                }

                is StateEvent.LoadManga -> {
                    _dataState = mediaRepository.getBrowseManga(filters)
                        .cachedIn(viewModelScope) as MutableLiveData<PagingData<Media>>
                }

                else -> {

                }
            }
        }
    }

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
}