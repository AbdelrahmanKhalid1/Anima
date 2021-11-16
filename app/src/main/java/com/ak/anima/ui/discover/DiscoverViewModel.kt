package com.ak.anima.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.anima.model.index.Media
import com.ak.anima.repository.MediaRepository
import com.ak.anima.utils.DataState
import com.ak.type.MediaSort
import com.ak.type.MediaStatus
import com.ak.type.MediaType
import com.apollographql.apollo.exception.ApolloNetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
) : ViewModel() {

    private val _mediaMutableList = MutableLiveData<DataState<ArrayList<List<Media>>>>()
    val mediaLiveData: LiveData<DataState<ArrayList<List<Media>>>> get() = _mediaMutableList


    fun getDiscoverMediaData(mediaType: MediaType) {
        viewModelScope.launch {
            try {
                _mediaMutableList.postValue(DataState.Loading)
                val dataList = ArrayList<List<Media>>()
                if (mediaType == MediaType.ANIME) {//load upcoming popular anime to be released
                    val allTimePopularList = mediaRepository.requestDiscoverMedia(
                        mediaType,
                        MediaStatus.NOT_YET_RELEASED,
                        MediaSort.POPULARITY_DESC
                    )
                    dataList.add(allTimePopularList)
                }

                //load all time popular media (anime or manga)
                var mediaList = mediaRepository.requestDiscoverMedia(
                    mediaType,
                    null,
                    MediaSort.POPULARITY_DESC
                )
                dataList.add(mediaList)

                //load top scored media (anime or manga)
                mediaList = mediaRepository.requestDiscoverMedia(
                    mediaType,
                    null,
                    MediaSort.SCORE_DESC
                )
                dataList.add(mediaList)
                _mediaMutableList.postValue(DataState.Success(dataList))

            } catch (ignore: ApolloNetworkException) {
                _mediaMutableList.postValue(DataState.Error(ignore))
            }
        }
    }
}