package com.ak.otaku_kun.model.remote.index

import com.ak.MediaBrowseQuery
import com.ak.otaku_kun.utils.Date

open class Media(
    val id: Int,
    val title: String,
    val image: String,
    val format: String,
    var isFavorite: Boolean,
    val status: String,
    val averageScore: Int,
//    val meanScore: Int,
//    val popularity: Int,
    val genre: String,
//    val origin: String,
//    val source: String,
//    val year: String,
//    val startDate: String,
//    val endDate: String,
    var mediaListEntry: MediaListEntry? = null
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

