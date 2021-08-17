package com.ak.otaku_kun.utils

sealed class StateEvent{
    object LoadAnime : StateEvent()
    object LoadManga : StateEvent()
    object Refresh : StateEvent()
}
