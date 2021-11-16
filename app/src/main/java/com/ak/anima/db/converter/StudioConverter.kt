package com.ak.anima.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

class StudioConverter {

    @TypeConverter
    fun fromStudioToString(studio: Pair<Int, String>?): String =
        Gson().toJson(studio)

    @TypeConverter
    fun fromStringToStudio(studioString: String): Pair<Int, String>? {
        val studio = Gson().fromJson(studioString, Pair::class.java)
        return studio as Pair<Int, String>?
    }
}