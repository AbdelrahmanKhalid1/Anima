package com.ak.anima.db.converter

import androidx.room.TypeConverter
import com.ak.anima.model.details.Media
import com.google.gson.Gson

class TrailerConverter {

    @TypeConverter
    fun fromTrailerToString(trailer: Media.Trailer?): String =
        Gson().toJson(trailer)

    @TypeConverter
    fun fromStringToTrailer(trailerString: String): Media.Trailer? =
        Gson().fromJson(trailerString, Media.Trailer::class.java)
}