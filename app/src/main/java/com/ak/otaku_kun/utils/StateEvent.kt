package com.ak.otaku_kun.utils

sealed class StateEvent{
    object LoadData : StateEvent()
    object Refresh : StateEvent()
}
