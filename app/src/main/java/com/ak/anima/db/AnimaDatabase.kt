package com.ak.anima.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ak.anima.db.converter.*
import com.ak.anima.db.dao.MediaDao
import com.ak.anima.db.entity.MediaCacheEntity

@Database(entities = [MediaCacheEntity::class], version = 1)
@TypeConverters(TitleConverter::class,
    TrailerConverter::class,
    StudioConverter::class,
    TagsConverter::class,
    ListEntryConverter::class)
abstract class AnimaDatabase : RoomDatabase() {

    abstract fun mediaDao(): MediaDao

    companion object {

        @Volatile
        private var instance: AnimaDatabase? = null

        fun getInstance(context: Context): AnimaDatabase {
            return instance ?: synchronized(this) {
                instance = Room.databaseBuilder(context, AnimaDatabase::class.java,
                    "anime_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                instance!!
            }
        }
    }
}