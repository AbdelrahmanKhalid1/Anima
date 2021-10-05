package com.ak.otaku_kun.utils

import com.ak.type.MediaType

sealed class StateEvent{
    object LoadData : StateEvent()
    object LoadMediaDetails : StateEvent()
    object Refresh : StateEvent()
    class InitQueryFilters(val mediaType: MediaType) : StateEvent()
//    object LoadData : StateEvent()
}