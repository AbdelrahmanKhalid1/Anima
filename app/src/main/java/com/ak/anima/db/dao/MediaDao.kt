package com.ak.anima.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.ak.anima.db.entity.MediaCacheEntity
import com.ak.anima.model.index.Media
import com.ak.type.MediaType

@Dao
interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewMedia(media: MediaCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMedias(medias: List<MediaCacheEntity>)

    @Update
    fun updateMediaCache(media: MediaCacheEntity)

    @Delete
    fun removeMedia(medias: MediaCacheEntity)

    @Delete
    fun removeListMedias(medias: List<MediaCacheEntity>)

    @Query("DELETE FROM medias")
    fun clearAll()

    @Query("SELECT * FROM medias WHERE media_list_status=:mediaListStatus AND media_type=:mediaType")
    fun pagingSource(mediaListStatus: String?, mediaType: String?): PagingSource<Int, Media>

    @Query("SELECT * FROM medias")
    fun getMedias(): List<MediaCacheEntity>

    @Query("SELECT * FROM medias WHERE media_id=:id")
    fun getMediaForId(id: Int): MediaCacheEntity

    @Query("SELECT COUNT(media_id) FROM medias WHERE media_id=:id")
    fun isMediaExist(id: Int): Int

    @Query("SELECT media_list_status FROM medias WHERE media_id=:id")
    fun getMediaListStatusForId(id: Int): String
}
