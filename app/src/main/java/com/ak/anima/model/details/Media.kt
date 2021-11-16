package com.ak.anima.model.details

import com.ak.anima.model.index.Character
import com.ak.anima.model.index.Staff
import com.ak.anima.utils.Const
import com.ak.anima.utils.Utils
import com.ak.anima.model.index.Media as MediaIndex
import com.ak.queries.media.MediaBrowseQuery

open class Media(
    val id: Int = -1,
    val title: Title,
    val format: String,
    val cover: String,
    val banner: String,
    val status: String,
    val meanScore: String,
    var isFavorite: Boolean,

//    val year: String,
    val genre: String,

    val startDate: String,
    val endDate: String,
    val source: String,
    val description: String,
    val studio: Pair<Int, String>,
    val season: String,
    val seasonYear: String,

    val averageScore: String,
    val popularity: Int,
    var mediaListEntry: MediaListEntry? = null,
    val siteUrl: String,
    val tags: List<Tag>,
    val trailer: Trailer?,
    val relations: List<Pair<String, List<MediaIndex>>>,
    val characters: List<Character>,
//    val stats
    val staff: List<Staff>,
//    val reviews
) {
    data class Title(
        var romaji: String = "",
        var english: String = "",
        var native: String = "",
        var userPreferred: String = "",
    )

    data class Trailer(
        val id: String = "",
        val site: String = "",
        val thumbnail: String = "",
    )

    data class Date(
        val day: Int,
        val month: Int,
        val year: Int,
    )

    data class Tag(
        val id: Int = Const.NO_ITEM,
        val name: String = "",
        val rank: Int = Const.NO_ITEM,
        val description: String = Const.NO_VALUE,
    )

    data class MediaListEntry(
        val id: Int,
//        val userId: Int,
//        val mediaId: Int,
        var progress: Int,
//        var reWatched: Int,
        var status: String,
        var score: Double,
        var startedAt: Date?,
        var completedAt: Date?,
        var progressVolume: Int = -1,
    ) {
        constructor(mediaListEntry: MediaBrowseQuery.MediaListEntry) : this(
            mediaListEntry.id,
//            userId,
//            mediaId,
            mediaListEntry.progress ?: 0,
            Utils.capitalizeFirstLetter(mediaListEntry.status?.rawValue),
            mediaListEntry.score ?: 0.0,
            mediaListEntry.startedAt?.let {
                Date(
                    it.day ?: Const.NO_ITEM,
                    it.month ?: Const.NO_ITEM,
                    it.year ?: Const.NO_ITEM
                )
            },
            mediaListEntry.completedAt?.let {
                Date(
                    it.day ?: Const.NO_ITEM,
                    it.month ?: Const.NO_ITEM,
                    it.year ?: Const.NO_ITEM
                )
            },
            mediaListEntry.progressVolumes ?: 0)
    }
}
