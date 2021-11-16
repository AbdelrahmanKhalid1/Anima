package com.ak.anima.utils

import android.content.Context
import android.util.TypedValue
import com.ak.queries.media.MediaQuery
import com.ak.type.MediaSeason
import com.ak.type.MediaType
import java.util.*

object Utils {

    @JvmStatic
    fun capitalizeFirstLetter(str: String?): String {
        if (str == null) {
            return Const.NO_VALUE
        }
        val firstLetter = str[0].uppercaseChar()
        val remaining = str.replace('_', ' ').substring(1).lowercase(Locale.ROOT)
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
    fun getDpValue(value: Float, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            context.resources.displayMetrics
        )
    }

//    @JvmStatic
//    fun formatDate(date: Date?): String {
//        if (date == null) {
//            return Const.NO_VALUE
//        }
//        return "${convertDateNumToString(date.month)} ${date.day}, ${date.year}"
//    }

    @JvmStatic
    fun formatDate(date: MediaQuery.StartDate?, str: String): String {
        date.run {
            if (this == null || (year == null && month == null && day == null)) {
                return Const.NO_VALUE
            }
            return "$str: ${convertDateNumToString(month)} ${day ?: ""}, ${year ?: ""}"
        }
    }

    @JvmStatic
    fun formatDate(date: MediaQuery.EndDate?, str: String): String {
        date.run {
            if (this == null || (year == null && month == null && day == null)) {
                return Const.NO_VALUE
            }
            return "$str: ${convertDateNumToString(month)} ${day ?: ""}, ${year ?: ""}"
        }
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
            else -> ""
        }

    @JvmStatic
    fun getSeason(
        media: MediaType?,
        season: MediaSeason?,
        startDate: MediaQuery.StartDate?,
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
        mediaType: MediaType?,
    ): String =
        when (mediaType) {
            MediaType.MANGA ->
                startDate?.year?.toString() ?: Const.NO_VALUE
            else ->
                animeSeasonYear?.toString() ?: Const.NO_VALUE
        }

    @JvmStatic
    fun formatNextAiringEpisode(timeUntilAiring: Int?, episode: Int): String {
        if (timeUntilAiring == null)
            return Const.NO_VALUE

        return when {
            timeUntilAiring < 60 -> { //less than 60 seconds
                "Ep $episode: 1m"
            }
            timeUntilAiring < 3600 -> { //60*60 = 3600 sec ==> less than 1 hour
                "Ep $episode: ${timeUntilAiring / 60 % 60}m"
            }
            timeUntilAiring < 86400 -> { //60*60*24 = 86400 sec ==> less than 1 day
                "Ep $episode: ${timeUntilAiring / (60 * 60) % 24}h ${timeUntilAiring / 60 % 60}m"
            }
            else -> {
                "Ep $episode: ${timeUntilAiring / (60 * 60 * 24)}d ${timeUntilAiring / (60 * 60) % 24}h ${timeUntilAiring / 60 % 60}m"
            }
        }
    }
}