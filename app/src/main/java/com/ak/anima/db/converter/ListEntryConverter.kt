package com.ak.anima.db.converter

import androidx.room.TypeConverter
import com.ak.anima.model.details.Media.MediaListEntry
import com.google.gson.Gson

class ListEntryConverter {

    @TypeConverter
    fun fromListEntryToString(listEntry: MediaListEntry?): String =
        Gson().toJson(listEntry)

    @TypeConverter
    fun fromStringToStudio(listEntryString: String): MediaListEntry? {
        return Gson().fromJson(listEntryString, MediaListEntry::class.java)
    }
}