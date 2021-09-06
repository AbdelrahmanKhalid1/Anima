package com.ak.otaku_kun.model.index

data class User (
    val id : Int = -1,
    val name : String = "",
    val avatar: String = "",
    val isFollowing : Boolean = false
)