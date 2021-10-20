package com.ak.otaku_kun.model.index

import com.ak.otaku_kun.model.details.Media

open class Media(
    val id: Int = -1,
    val title: String = "",
    val cover: String= "",
    val format: String= "",
    var isFavorite: Boolean = false,
    val status: String= "", //todo
    val averageScore: String = "0",
//    val meanScore: Int,
//    val popularity: Int,
    val genre: String = "",
//    val origin: String,
//    val source: String,
//    val year: String,
//    val startDate: String,
//    val endDate: String,
    var mediaListEntry: Media.MediaListEntry? = null
)

