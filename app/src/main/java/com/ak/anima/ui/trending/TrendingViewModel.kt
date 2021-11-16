package com.ak.anima.ui.trending

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
class TrendingViewModel @Inject constructor(private val mediaRepository: MediaRepository) :
    ViewModel() {

    var data: LiveData<PagingData<Media>> = MutableLiveData()
    //TODO make member to know which fragment in viewpager to continue from
//    private val page = MutableLiveData(1)
//    var scrollPosition = 0

    fun getTrendingMedias(mediaType: MediaType) {
        viewModelScope.launch {
            data = mediaRepository.requestTrendingMedia(mediaType).cachedIn(viewModelScope)
        }
    }
}