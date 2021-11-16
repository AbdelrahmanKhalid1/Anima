package com.ak.anima.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import  com.ak.anima.model.details.Media.Tag
import  com.ak.anima.model.details.Media.Trailer
import  com.ak.anima.model.details.Media.Title
import  com.ak.anima.model.details.Media.MediaListEntry
import com.ak.anima.model.index.Media as MediaIndex
import com.ak.anima.utils.Const

@Entity(tableName = "medias")
data class MediaCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "media_id")
    var id: Int = Const.NO_ITEM,

    @ColumnInfo(name = "media_type")
    var type: String = "",

    @ColumnInfo(name = "media_title")
    var title: Title = Title(),

    @ColumnInfo(name = "format")
    var format: String = "",

    @ColumnInfo(name = "cover")
    var cover: String = "",

    @ColumnInfo(name = "banner")
    var banner: String = "",

    @ColumnInfo(name = "status")
    var status: String = "",

    @ColumnInfo(name = "mean_score")
    var meanScore: Int = 0,

    @ColumnInfo(name = "if_favorite")
    var isFavorite: Boolean = false,

    @ColumnInfo(name = "genre")
    var genre: String = "",

    @ColumnInfo(name = "start_data")
    var startDate: String = "",

    @ColumnInfo(name = "end_date")
    var endDate: String = "",

    @ColumnInfo(name = "source")
    var source: String = "",

    @ColumnInfo(name = "desc")
    var description: String = "",

    @ColumnInfo(name = "studio")
    var studio: Pair<Int, String>? = null,

    @ColumnInfo(name = "season")
    var season: String = "",

    @ColumnInfo(name = "season_year")
    var seasonYear: String = "",

    @ColumnInfo(name = "avg_score")
    var averageScore: Int = 0,

    @ColumnInfo(name = "popularity")
    var popularity: Int = 0,

//    @Ignore
    @ColumnInfo(name = "list_entry")
    var mediaListEntry: MediaListEntry? = null,

    @ColumnInfo(name = "url")
    var siteUrl: String = "",

    @ColumnInfo(name = "tags")
//    @Ignore
    var tags: List<Tag>? = null,

    @ColumnInfo(name = "trailer")
    var trailer: Trailer? = null,

    @Ignore
    var relations: Map<String, List<MediaIndex>>? = null,

//    @ForeignKey(entity = QueueEntity::class, parentColumns = ["queue_id"], childColumns = ["queue_id"])
//    @ColumnInfo(name = "queue_id")
//    var queueId: Int = -1

    @ColumnInfo(name = "episodes")
    var episodes: String = Const.NO_VALUE,

    @ColumnInfo(name = "chapters")
    var chapters: String = Const.NO_VALUE,

    @ColumnInfo(name = "duration")
    var duration: String = Const.NO_VALUE,

    @ColumnInfo(name = "volumes")
    var volumes: String = Const.NO_VALUE,

    @ColumnInfo(name = "time_until_airing")
    var timeUntilAiring: String = "",

    @ColumnInfo(name = "media_list_status")
    var mediaListStatus: String? = null,
)