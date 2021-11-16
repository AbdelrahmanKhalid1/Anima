package com.ak.anima.db.converter

import androidx.room.TypeConverter
import com.ak.anima.model.details.Media.Tag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class TagsConverter {
    @TypeConverter
    fun fromTagsToString(tags: List<Tag>?): String =
        Gson().toJson(tags)

    @TypeConverter
    fun fromStringToStudio(tagsString: String): List<Tag>? {
        val listType = object : TypeToken<List<Tag>?>(){}.type
        return Gson().fromJson(tagsString, listType)
    }
}