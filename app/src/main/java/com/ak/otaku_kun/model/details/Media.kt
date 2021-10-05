package com.ak.otaku_kun.model.details

import com.ak.otaku_kun.model.index.Character
import com.ak.otaku_kun.model.index.Staff
import com.ak.otaku_kun.utils.Const
import com.ak.quries.media.MediaBrowseQuery
import java.io.Serializable

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
    val studio: String,
    val season: String,
    val seasonYear: String,

    val averageScore: String = "0",
    val popularity: Int,
    var mediaListEntry: MediaListEntry? = null,
    val siteUrl: String,
    val tags: List<Tag>,
    val trailer: Trailer?,
//    val relations
    val characters: List<Character>,
//    val stats
    val staff: List<Staff>,
//    val reviews
) {

    data class Title(
        val romaji: String,
        val english: String,
        val native: String,
        val userPreferred: String
    ) {
        companion object {
            @JvmStatic
            fun newTitle(
                romaji: String?,
                english: String?,
                native: String?,
                userPreferred: String?
            ): Title = Title(
                romaji ?: Const.NO_VALUE,
                english ?: Const.NO_VALUE,
                native ?: Const.NO_VALUE,
                userPreferred ?: Const.NO_VALUE
            )
        }
    }

    data class Trailer(
        val id: String = "",
        val site: String = "",
        val thumbnail: String = ""
    )

    data class Date(
        val day: Int,
        val month: Int,
        val year: Int
    )

    data class Tag(
        val id:Int = Const.NO_ITEM,
        val name: String = "",
        val rank: Int = Const.NO_ITEM,
        val description: String = Const.NO_VALUE
    )

    data class MediaListEntry(
        val id: Int,
        val userId: Int,
        val mediaId: Int,
        var progress: Int,
        var status: String,
        var score: Double,
        var startedAt: Date?,
        var completedAt: Date?,
        var progressVolume: Int = -1
    ) {

        companion object {
            @JvmStatic
            fun newInstance(mediaListEntry: MediaBrowseQuery.MediaListEntry?): MediaListEntry? {
                return if (mediaListEntry == null) null else MediaListEntry(
                    mediaListEntry.id,
                    mediaListEntry.userId,
                    mediaListEntry.mediaId,
                    mediaListEntry.progress ?: 0,
                    mediaListEntry.status?.rawValue ?: "Planning",
                    mediaListEntry.score ?: 0.0,
                    if (mediaListEntry.startedAt == null) null else
                        Date(
                            mediaListEntry.startedAt.day!!,
                            mediaListEntry.startedAt.month!!,
                            mediaListEntry.startedAt.year!!
                        ),
                    if (mediaListEntry.completedAt == null) null else
                        Date(
                            mediaListEntry.completedAt.day!!,
                            mediaListEntry.completedAt.month!!,
                            mediaListEntry.completedAt.year!!
                        ),
                    mediaListEntry.progressVolumes ?: 0
                )
            }
        }
    }
}