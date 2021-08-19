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

private const val TAG = "BrowseMediaViewModel"

@HiltViewModel
class BrowseMediaViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    var queryFilters: QueryFilters = QueryFilters()
) :
    ViewModel() {

    var dataState: LiveData<PagingData<Media>> = MutableLiveData()
    //TODO make member to know which fragment in viewpager to continue from
//    private val page = MutableLiveData(1)
//    var scrollPosition = 0

    fun onTriggerStateEvent(stateEvent: StateEvent) {
        viewModelScope.launch {
            when (stateEvent) {
                is StateEvent.LoadAnime -> {
                    dataState = mediaRepository.requestBrowseAnime(queryFilters)
                        .cachedIn(viewModelScope)
                }
                is StateEvent.LoadManga -> {
                    dataState = mediaRepository.requestBrowseManga(queryFilters)
                        .cachedIn(viewModelScope)
                }
                else -> {

                }
            }
        }
    }
}