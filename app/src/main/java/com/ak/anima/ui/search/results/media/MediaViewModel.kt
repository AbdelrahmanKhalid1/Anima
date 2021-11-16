package com.ak.anima.ui.search.results.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.anima.model.index.Media
import com.ak.anima.repository.MediaRepository
import com.ak.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    var searchResult: LiveData<PagingData<Media>> = MutableLiveData()
//    var scrollPosition = 0

    fun findMediaByName(mediaType: MediaType, query: String) {
        viewModelScope.launch {
            searchResult = mediaRepository.searchMedia(mediaType, query)
                .cachedIn(viewModelScope)
        }
    }
}
