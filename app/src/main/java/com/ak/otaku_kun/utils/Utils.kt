package com.ak.otaku_kun.utils

import com.ak.otaku_kun.model.details.Media
import com.ak.otaku_kun.model.details.Media.Date
import com.ak.quries.media.MediaQuery
import com.ak.type.MediaSeason
import com.ak.type.MediaType
import java.util.*

object Utils {

    @JvmStatic
    fun capitalizeFirstLetter(str: String?): String {
        if (str == null) {
            return Const.NO_VALUE
        }
        val firstLetter = str[0].toUpperCase()
        val remaining = str.replace('_', ' ').substring(1).toLowerCase(Locale.ROOT)
        return "$firstLetter$remaining"
    }

    @JvmStatic
    fun formatScore(score: Int?): String {
        if (score == null)
            return "0.0"
        //TODO can edit how to show score ex: 8.8, 88%
        return (score / 10f).toString()
    }

    @JvmStatic
    fun formatDate(date: Date?): String {
        if (date == null) {
            return Const.NO_VALUE
        }
        return "${convertDateNumToString(date.month)} ${date.day}, ${date.year}"
    }

    @JvmStatic
    fun formatDate(date: MediaQuery.StartDate?, str: String): String {
        if (date == null) {
            return Const.NO_VALUE
        }
        return "$str: ${convertDateNumToString(date.month)} ${date.day}, ${date.year}"
    }

    @JvmStatic
    fun formatDate(date: MediaQuery.EndDate?, str: String): String {
        if (date?.year == null || date.month == null || date.day == null) {
            return Const.NO_VALUE
        }
        return "$str: ${convertDateNumToString(date.month)} ${date.day}, ${date.year}"
    }

    @JvmStatic
    fun convertDateNumToString(month: Int?): String =
        when (month) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> Const.NO_VALUE
        }

    @JvmStatic
    fun getSeason(
        media: MediaType?,
        season: MediaSeason?,
        startDate: MediaQuery.StartDate?
    ): String {
        return when (media) {
            MediaType.MANGA -> getSeasonFromMonth(startDate?.month)
            MediaType.ANIME -> capitalizeFirstLetter(season?.rawValue)
            else -> Const.NO_VALUE
        }
    }

    @JvmStatic
    fun getSeasonFromMonth(month: Int?): String =
        when (month) {
            12, 1, 2 -> "Winter"
            3, 4, 5 -> "Spring"
            6, 7, 8 -> "Summer"
            9, 10, 11 -> "Fall"
            else -> Const.NO_VALUE
        }

    @JvmStatic
    fun setSeasonYear(
        animeSeasonYear: Int?,
        startDate: MediaQuery.StartDate?,
        mediaType: MediaType?
    ): String =
        when (mediaType) {
            MediaType.MANGA ->
                startDate?.year?.toString() ?: Const.NO_VALUE
            else ->
                animeSeasonYear?.toString() ?: Const.NO_VALUE
        }

    @JvmStatic
    fun mapTrailer(trailer: MediaQuery.Trailer?): Media.Trailer? {
        if (trailer == null)
            return null
        return Media.Trailer(trailer.id!!, trailer.site!!, trailer.thumbnail ?: "")
    }

    @JvmStatic
    fun mapTag(tag: MediaQuery.Tag): Media.Tag =
        Media.Tag(
            id = tag.id,
            name = tag.name,
//            rank = tag.rank,
//            description = tag.description
        )
}