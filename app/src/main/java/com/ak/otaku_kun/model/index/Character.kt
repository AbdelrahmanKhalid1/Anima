package com.ak.otaku_kun.model.index

data class Character (
    val id : Int = -1,
    val name : String = "",
    val image: String = "",
    val isFavorite: Boolean = false,
    val favorites: Int = 0,
    var role: String = ""
)