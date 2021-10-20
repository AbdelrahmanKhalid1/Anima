package com.ak.otaku_kun.ui.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.repository.MediaRepository
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.otaku_kun.utils.StateEvent
import com.ak.type.MediaSort
import com.ak.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BrowseMediaViewModel"

@HiltViewModel
class BrowseMediaViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
) :
    ViewModel() {

    var mediaData: LiveData<PagingData<Media>> = MutableLiveData()
    private val _queryFilters: MutableLiveData<QueryFilters> = MutableLiveData()
    val queryFilters: LiveData<QueryFilters> get() = _queryFilters

    //TODO make member to know which fragment in viewpager to continue from
//    private val page = MutableLiveData(1)
//    var scrollPosition = 0

    fun onTriggerStateEvent(stateEvent: StateEvent) {
        viewModelScope.launch {
            when (stateEvent) {
                is StateEvent.LoadData -> {
                    mediaData = mediaRepository.requestBrowseMedia(queryFilters.value!!)
                        .cachedIn(viewModelScope)
                }
                is StateEvent.Refresh -> {

                }
                is StateEvent.InitQueryFilters ->{
                    _queryFilters.value = QueryFilters.newInstance(stateEvent.mediaType)
                }
                else ->{}
            }
        }
    }

    fun setQueryFilters(queryFilters: QueryFilters) {
        _queryFilters.value = queryFilters
    }

    fun updateQueryFilters(sort: List<MediaSort>) {
        val filters = _queryFilters.value
        filters?.apply {
            listSort = sort
        }
        _queryFilters.value = filters
    }

    fun getQueryFilters() = _queryFilters.value!!
}