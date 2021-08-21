package com.ak.otaku_kun.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.repository.MediaRepository
import com.ak.type.MediaSort
import com.ak.type.MediaStatus
import com.ak.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
) : ViewModel() {

    private val _data = MutableLiveData<ArrayList<List<Media>>>()
    val data: LiveData<ArrayList<List<Media>>> get() = _data

    fun getDiscoverMediaData(mediaType: MediaType) {
        viewModelScope.launch {

            val dataList = ArrayList<List<Media>>()
            if (mediaType == MediaType.ANIME) {
                val upcoming = async {
                    mediaRepository.requestDiscoverMedia(
                        mediaType,
                        MediaStatus.NOT_YET_RELEASED,
                        MediaSort.POPULARITY_DESC
                    )
                }
                dataList.addAll(listOf(upcoming.await()))
            }

            val allTimePopular = async {
                mediaRepository.requestDiscoverMedia(
                    mediaType,
                    null,
                    MediaSort.POPULARITY_DESC
                )
            }
            val topScored = async {
                mediaRepository.requestDiscoverMedia(
                    mediaType,
                    null,
                    MediaSort.SCORE_DESC
                )
            }
            dataList.addAll(listOf(allTimePopular.await()))
            dataList.addAll(listOf(topScored.await()))
            _data.postValue(dataList)
        }
    }

}