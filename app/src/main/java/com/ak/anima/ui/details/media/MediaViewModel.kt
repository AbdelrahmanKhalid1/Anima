package com.ak.anima.ui.details.media

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.anima.model.details.Media
import com.ak.anima.model.index.Review
import com.ak.anima.repository.MediaRepository
import com.ak.anima.utils.Const
import com.ak.anima.utils.DataState
import com.ak.anima.utils.StateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MediaViewModel"

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

    //===========================================================================================
    //media character members section
    private val _mediaCharacterPageInfo = MutableLiveData<Pair<Int, Boolean>>()
    private val _mediaCharactersLiveData =
        MutableLiveData<DataState<Any>>() //DataState<List<Pair<String, ArrayList<Character>>>>
    val mediaCharactersLiveData: LiveData<DataState<Any>>
        get() = _mediaCharactersLiveData

    //===========================================================================================
    //media staff members section
    private val _mediaStaffPageInfo = MutableLiveData<Pair<Int, Boolean>>()
    private val _mediaStaffLiveData =
        MutableLiveData<DataState<Any>>() //DataState<List<Pair<String, ArrayList<Character>>>>
    val mediaStaffLiveData: LiveData<DataState<Any>>
        get() = _mediaStaffLiveData

    //===========================================================================================
    //media staff members section
    var mediaReviewLiveData: LiveData<PagingData<Review>> = MutableLiveData()

    fun triggerStateEvent(stateEvent: StateEvent) {
        val mediaId = _mediaId.value ?: return
        viewModelScope.launch {
            when (stateEvent) {
                StateEvent.LoadMediaCharacters -> {
                    if (_mediaCharacterPageInfo.value == null) {
                        _mediaCharacterPageInfo.value = Pair(1, true)
                    }
                    val pageInfo = _mediaCharacterPageInfo.value!!
                    if (!pageInfo.second) //has no next page (is last page)
                        return@launch
                    Log.d(TAG, "triggerMediaCharacterEvent: ${pageInfo.first}")
                    mediaRepository.requestMediaCharacter(mediaId, pageInfo.first)
                        .onEach { dataState ->
                            fetchData(dataState, _mediaCharacterPageInfo, _mediaCharactersLiveData)
                        }.launchIn(viewModelScope)
                }
                StateEvent.LoadMediaStaff -> {
                    if (_mediaStaffPageInfo.value == null) {
                        _mediaStaffPageInfo.value = Pair(1, true)
                    }
                    val pageInfo = _mediaStaffPageInfo.value!!
                    if (!pageInfo.second) //has no next page (is last page)
                        return@launch
                    Log.d(TAG, "triggerMediaStaffEvent: ${pageInfo.first}")
                    mediaRepository.requestMediaStaff(mediaId, pageInfo.first)
                        .onEach { dataState ->
                            fetchData(dataState, _mediaStaffPageInfo, _mediaStaffLiveData)
                        }.launchIn(viewModelScope)
                }
                StateEvent.LoadMediaReviews -> {
                    Log.d(TAG, "triggerMediaReviewsEvent:")
                    mediaReviewLiveData = mediaRepository.requestMediaReviews(mediaId).cachedIn(viewModelScope)
                }
                else -> { }
            }
        }
    }

    /**
     * function to fetch data of media characters and media staff
     *
     * @param dataState DataState<Pair<Boolean, List<Items>>>
     * @param pageInfoMutableData PageInfo of current requested media items
     * @param mediaChildMutableData The LiveData which value is observed by parent fragment
     **/
    private fun fetchData(
        dataState: DataState<Any>,
        pageInfoMutableData: MutableLiveData<Pair<Int, Boolean>>,
        mediaChildMutableData: MutableLiveData<DataState<Any>>,
    ) {
        when (dataState) {
            is DataState.Success -> {
                val dataHolder = dataState.data as Pair<Boolean, List<Any>>
                val oldPageInfo = pageInfoMutableData.value
                oldPageInfo?.run {
                    val newPageInfo = Pair(oldPageInfo.first + 1, dataHolder.first)
                    pageInfoMutableData.value = newPageInfo
                }
                mediaChildMutableData.value = DataState.Success(dataHolder.second)
            }
            else -> mediaChildMutableData.value = dataState
        }
    }

    fun cancelJob() {
        mediaRepository.cancelJobs()
    }

    override fun onCleared() {
        mediaRepository.cancelJobs()
        super.onCleared()
    }
}