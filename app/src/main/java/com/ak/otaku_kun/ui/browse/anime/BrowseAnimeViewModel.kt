package com.ak.otaku_kun.ui.browse.anime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.otaku_kun.model.remote.index.Anime
import com.ak.otaku_kun.repository.MediaRepository
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.otaku_kun.utils.StateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseAnimeViewModel @Inject constructor(private val mediaRepository: MediaRepository,) :
    ViewModel() {
    var scrollPosition = 0
    private var _dataState = MutableLiveData<PagingData<Anime>>()
    val dataState: LiveData<PagingData<Anime>> get() = _dataState
    //TODO make member to know which fragment in viewpager to continue from
    //private val page = MutableLiveData(1)

    fun onTriggerStateEvent(stateEvent: StateEvent, filters: QueryFilters) {
        viewModelScope.launch {
            when (stateEvent) {
                is StateEvent.LoadData -> {
                    _dataState = mediaRepository.getBrowseAnime(filters)
                        .cachedIn(viewModelScope) as MutableLiveData<PagingData<Anime>>
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