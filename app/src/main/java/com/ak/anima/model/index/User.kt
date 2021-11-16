package com.ak.anima.model.index

data class User (
    val id : Int = -1,
    val name : String = "",
    val avatar: String = "",
    val isFollowing : Boolean = false
)