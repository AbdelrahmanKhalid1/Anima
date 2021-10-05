package com.ak.otaku_kun.ui.details.media

import androidx.lifecycle.*
import com.ak.otaku_kun.model.details.Media
import com.ak.otaku_kun.repository.MediaRepository
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.StateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val mediaRepository: MediaRepository) :
    ViewModel() {

    private val _mediaId = MutableLiveData<Int>()
    val mediaLiveData: LiveData<Media> = Transformations.switchMap(_mediaId) { mediaId ->
        mediaRepository.requestMediaDetails(mediaId)
    }

    fun setMediaId(mediaId: Int) {
        if (_mediaId.value != mediaId && mediaId != Const.NO_ITEM)
            _mediaId.value = mediaId
    }

    fun cancelJob(){
        mediaRepository.cancelJobs()
    }

    override fun onCleared() {
        mediaRepository.cancelJobs()
        super.onCleared()
    }
}