package com.ak.anima.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ak.anima.model.details.Media.Title

class TitleConverter {

    @TypeConverter
    fun fromTitleToString(title: Title): String =
        Gson().toJson(title)

    @TypeConverter
    fun fromStringToTitle(titleString: String): Title =
        Gson().fromJson(titleString, Title::class.java)
}