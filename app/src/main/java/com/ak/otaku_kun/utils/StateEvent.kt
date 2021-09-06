package com.ak.otaku_kun.utils

sealed class StateEvent{
    object LoadMedia : StateEvent()
    object Refresh : StateEvent()
}