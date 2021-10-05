package com.ak.otaku_kun.utils

import android.os.Parcelable
import android.util.Log
import com.ak.type.*
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "QueryFilters"

@Parcelize
data class QueryFilters(
    //TODO can be converted into sealed class
    //number of episodes or manga by number of volumes and chapters
    var type: MediaType? = null,
    var format: MediaFormat? = null,
    var status: MediaStatus? = null,
    var season: MediaSeason? = null,
    var seasonYear: Int? = null,
    var startDate: String? = null,
    var source: MediaSource? = null,
    var listGenre: List<String>? = null,
    var listSort: List<MediaSort>? = null
) : Parcelable {

    fun getGenre(): List<String> = if (listGenre == null) emptyList() else listGenre!!

    fun printData(): String {
        return """Type: $type Format: $format Status: $status  Source: $source seasonYear: $seasonYear
                  StartYear: $startDate Genre:${listGenre} Sort:${listSort} Season: $season"""
    }

    companion object {
        @JvmStatic
        fun newInstance(mediaType: MediaType): QueryFilters {

            val queryFilters = when (mediaType) {
                MediaType.MANGA -> QueryFilters(
                    type = MediaType.MANGA,
                    startDate = "${Calendar.getInstance().get(Calendar.YEAR)}%"
                )
                else -> QueryFilters(
                    type = MediaType.ANIME,
                    seasonYear = Calendar.getInstance().get(Calendar.YEAR)
                ).apply {
                    season = when (Calendar.getInstance().get(Calendar.MONTH)+1) {//+1 as months are zero based
                        12, 1, 2 -> MediaSeason.WINTER
                        3, 4, 5 -> MediaSeason.SPRING
                        6, 7, 8 -> MediaSeason.SUMMER
                        9, 10, 11 -> MediaSeason.FALL
                        else -> null
                    }
                }
            }

            queryFilters.apply {
                val listSort = ArrayList<MediaSort>(1)
                listSort.add(MediaSort.POPULARITY_DESC)
                Log.d(TAG, "ListSortSize: ${listSort.size}, seasonYear = $season")
                this.listSort = listSort
            }
            return queryFilters
        }
    }
}

class QueryFilterHelper(val queryFilters: QueryFilters) {

    fun getTypeIndex(): Int {
        return when (queryFilters.type) {
            MediaType.ANIME -> 0
            MediaType.MANGA -> 1
            else -> 0
        }
    }

    fun getFormatIndex(): Int =
        if (queryFilters.type == MediaType.ANIME) {
            when (queryFilters.format) {
                MediaFormat.TV -> 1
                MediaFormat.TV_SHORT -> 2
                MediaFormat.MOVIE -> 3
                MediaFormat.SPECIAL -> 4
                MediaFormat.OVA -> 5
                MediaFormat.ONA -> 6
                MediaFormat.MUSIC -> 7
                else -> 0 //ALL
            }
        } else
            when (queryFilters.format) {
                MediaFormat.MANGA -> 1
                MediaFormat.NOVEL -> 2
                MediaFormat.ONE_SHOT -> 3
                else -> 0 //ALL
            }


    fun getSeasonIndex(): Int {
        return when (queryFilters.season) {
            MediaSeason.WINTER -> 1
            MediaSeason.SPRING -> 2
            MediaSeason.SUMMER -> 3
            MediaSeason.FALL -> 4
            else -> 0 //ALL
        }
    }

    fun getSourceIndex(): Int {
        return when (queryFilters.source) {
            MediaSource.ORIGINAL -> 1
            MediaSource.MANGA -> 2
            MediaSource.NOVEL -> 3
            MediaSource.ANIME -> 4
            MediaSource.LIGHT_NOVEL -> 5
            MediaSource.VISUAL_NOVEL -> 6
            MediaSource.VIDEO_GAME -> 7
            MediaSource.DOUJINSHI -> 8
            MediaSource.OTHER -> 9
            else -> 0 //ALL
        }
    }

    fun getStatusIndex(): Int {
        return when (queryFilters.status) {
            MediaStatus.FINISHED -> 1
            MediaStatus.RELEASING -> 2
            MediaStatus.NOT_YET_RELEASED -> 3
            MediaStatus.CANCELLED -> 4
            MediaStatus.HIATUS -> 5
            else -> 0 //ALL
        }
    }

    fun getYear(): Int? =
        if (queryFilters.type == MediaType.ANIME) queryFilters.seasonYear else queryFilters.startDate!!.substring(
            0,
            4
        ).toInt()

    fun getGenreCount(): String =
        if (queryFilters.listGenre != null) "${queryFilters.listGenre?.size} Selected" else "0 Selected"

    fun setFormat(selectedItemPosition: Int) {
        queryFilters.format =
            if (queryFilters.type == MediaType.ANIME) setAnimeFormat(selectedItemPosition)
            else setMangeFormat(selectedItemPosition)
    }

    private fun setAnimeFormat(selectedItemPosition: Int): MediaFormat? =
        when (selectedItemPosition) {
            1 -> MediaFormat.TV
            2 -> MediaFormat.TV_SHORT
            3 -> MediaFormat.MOVIE
            4 -> MediaFormat.SPECIAL
            5 -> MediaFormat.OVA
            6 -> MediaFormat.ONA
            7 -> MediaFormat.MUSIC
            else -> null
        }

    private fun setMangeFormat(selectedItemPosition: Int): MediaFormat? =
        when (selectedItemPosition) {
            1 -> MediaFormat.MANGA
            2 -> MediaFormat.NOVEL
            3 -> MediaFormat.ONE_SHOT
            else -> null
        }

    fun setStatus(selectedItemPosition: Int) {
        queryFilters.status = when (selectedItemPosition) {
            1 -> MediaStatus.FINISHED
            2 -> MediaStatus.RELEASING
            3 -> MediaStatus.NOT_YET_RELEASED
            4 -> MediaStatus.NOT_YET_RELEASED
            5 -> MediaStatus.HIATUS
            else -> null
        }
    }

    fun setSeason(selectedItemPosition: Int) {
        queryFilters.season = when (selectedItemPosition) {
            1 -> MediaSeason.WINTER
            2 -> MediaSeason.SPRING
            3 -> MediaSeason.SUMMER
            4 -> MediaSeason.FALL
            else -> null
        }
    }

    fun setYear(year: Int, isAllTime: Boolean) {
        when (queryFilters.type) {
            MediaType.MANGA -> {
                queryFilters.startDate =
                    if (isAllTime) null else "$year%"
            }
            else -> {
                queryFilters.seasonYear =
                    if (isAllTime) null else year
            }
        }
    }

    fun setSource(selectedItemPosition: Int) {
        queryFilters.source = when (selectedItemPosition) {
            1 -> MediaSource.ORIGINAL
            2 -> MediaSource.MANGA
            3 -> MediaSource.NOVEL
            4 -> MediaSource.ANIME
            5 -> MediaSource.LIGHT_NOVEL
            6 -> MediaSource.VISUAL_NOVEL
            7 -> MediaSource.VIDEO_GAME
            8 -> MediaSource.DOUJINSHI
            9 -> MediaSource.OTHER
            else -> null
        }
    }

    fun setGenres(listGenre: List<String>) {
        if (listGenre.isEmpty())
            queryFilters.listGenre = null
        else
            queryFilters.listGenre = listGenre
    }
}