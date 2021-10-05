package com.ak.otaku_kun.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ak.otaku_kun.model.details.Media

@Entity(tableName = "song")
data class MediaCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "media_id")
    var id: Int,

    @ColumnInfo(name = "media_type")
    var type: String,

    @ColumnInfo(name = "media_title")
    val title: Media.Title,

    @ColumnInfo(name = "format")
    val format: String,

    @ColumnInfo(name = "cover")
    val cover: String,

    @ColumnInfo(name = "banner")
    val banner: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "mean_score")
    val meanScore: Int,

    @ColumnInfo(name = "if_favorite")
    var isFavorite: Boolean,

//    @ColumnInfo(name = "media_id")
//    val year: String,

    @ColumnInfo(name = "genre")
    val genre: String,

    @ColumnInfo(name = "start_data")
    val startDate: String,

    @ColumnInfo(name = "end_date")
    val endDate: String,

    @ColumnInfo(name = "source")
    val source: String,

    @ColumnInfo(name = "desc")
    val description: String,

    @ColumnInfo(name = "studio")
    val studio: String,

    @ColumnInfo(name = "season")
    val season: String,

    @ColumnInfo(name = "season_year")
    val seasonYear: String,

    @ColumnInfo(name = "avg_score")
    val averageScore: Int,

    @ColumnInfo(name = "popularity")
    val popularity: Int,

    @ColumnInfo(name = "list_entry")
    var mediaListEntry: Media.MediaListEntry? = null,

    @ColumnInfo(name = "url")
    val siteUrl: String,

    @ColumnInfo(name = "tags")
    val tags: List<Media.Tag>,

    @ColumnInfo(name = "trailer")
    val trailer: Media.Trailer?,
//    val relations

//    @ColumnInfo(name = "characters")
//    val characters: List<Character>,
//    val stats

//    @ColumnInfo(name = "staff")
//    val staff: List<Staff>,
//    val reviews

//    @ForeignKey(entity = QueueEntity::class, parentColumns = ["queue_id"], childColumns = ["queue_id"])
//    @ColumnInfo(name = "queue_id")
//    var queueId: Int = -1

    @ColumnInfo(name = "episodes")
    val episodes: String,

    @ColumnInfo(name = "chapters")
    val chapters: String,

    @ColumnInfo(name = "duration")
    val duration: String,

    @ColumnInfo(name = "volumes")
    val volumes: String,
)