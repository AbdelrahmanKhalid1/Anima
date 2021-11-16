package com.ak.anima.ui.user_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.anima.model.details.AuthUser
import com.ak.anima.model.index.Media
import com.ak.anima.repository.MediaRepository
import com.ak.anima.utils.StateEvent
import com.ak.type.MediaListStatus
import com.ak.type.MediaType
import com.ak.type.ScoreFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMediaListViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val _authUser: AuthUser?,
) : ViewModel() {

    var mediaList: LiveData<PagingData<Media>> = MutableLiveData()
    private val authUser: AuthUser get() = _authUser!!
    //TODO make member to know which fragment in viewpager to continue from
//    private val page = MutableLiveData(1)
//    var scrollPosition = 0

    @OptIn(ExperimentalPagingApi::class)
    fun onTriggerStateEvent(
        stateEvent: StateEvent,
        mediaType: MediaType?,
        mediaListStatus: MediaListStatus?,
    ) {
        viewModelScope.launch {
            when (stateEvent) {
                is StateEvent.LoadData -> {
                    mediaList = mediaRepository.requestUserMediaList(mediaType,
                        mediaListStatus,
                        authUser.id,
                        authUser.mediaLIstOptions?.scoreFormat ?: ScoreFormat.POINT_10_DECIMAL)
                        .cachedIn(viewModelScope)
                }
//                is StateEvent.Refresh -> { }
                else -> { }
            }
        }
    }
}