package com.ak.anima.utils

import com.ak.type.MediaType

sealed class StateEvent{
    object LoadData : StateEvent()
    object LoadMoreData : StateEvent()
    object LoadMediaDetails : StateEvent()
    object LoadMediaCharacters : StateEvent()
    object LoadMediaStaff : StateEvent()
    object LoadMediaReviews : StateEvent()
    object Refresh : StateEvent()
    class InitQueryFilters(val mediaType: MediaType) : StateEvent()
}