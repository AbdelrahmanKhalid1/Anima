package com.ak.anima.model.index

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.ak.anima.model.details.Media

open class Media(
    @ColumnInfo(name = "media_id")
    var id: Int = -1,
    @ColumnInfo(name = "media_title")
    var title: Media.Title = Media.Title(),
    @ColumnInfo(name = "cover")
    var cover: String = "",
    @ColumnInfo(name = "format")
    var format: String = "",
    @ColumnInfo(name = "if_favorite")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "status")
    var status: String = "", //todo
    @ColumnInfo(name = "avg_score")
    var averageScore: String = "0",
    @ColumnInfo(name = "time_until_airing")
    var genre: String = "",
    @Ignore
    var mediaListEntry: Media.MediaListEntry? = null,
)