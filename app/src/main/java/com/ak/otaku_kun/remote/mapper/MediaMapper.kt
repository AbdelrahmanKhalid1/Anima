package com.ak.otaku_kun.remote.mapper

import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.model.index.MediaListEntry
import com.ak.otaku_kun.utils.Mapper
import com.ak.quries.media.MediaBrowseQuery
import com.ak.quries.media.MediaSearchQuery
import javax.inject.Inject

class MediaMapper @Inject constructor(){
    val mediaBrowseMapper = MediaBrowseMapper()
    val mediaSearchMapper = MediaSearchMapper()
}

class MediaBrowseMapper:Mapper<MediaBrowseQuery.Medium?, Media>{

//    val browseAnimeMapper = BrowseAnimeMapper()
//    val browseMangaMapper = BrowseMangaMapper()

    override fun mapFromEntityToModel(entity: MediaBrowseQuery.Medium?): Media {
        if (entity != null) {
            return Media(
                entity.id,
                entity.title?.userPreferred ?: "",
                entity.coverImage?.extraLarge ?: entity.coverImage?.large ?: "",
                entity.format?.rawValue ?: "",
                entity.isFavourite,
                entity.status?.rawValue ?: "",
                entity.averageScore ?: 0,
                entity.genres.toString().also { it.substring(1, it.length - 1) },
                MediaListEntry.newInstance(entity.mediaListEntry)
            )
        }
        return Media()
    }

    override fun mapFromModelToEntity(model: Media): MediaBrowseQuery.Medium? {
        return null
    }

    fun mapFromEntityList(entities: List<MediaBrowseQuery.Medium?>?): List<Media> {
        return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
    }
}

class MediaSearchMapper : Mapper<MediaSearchQuery.Medium?, Media> {

//    val browseAnimeMapper = BrowseAnimeMapper()
//    val browseMangaMapper = BrowseMangaMapper()

    override fun mapFromEntityToModel(entity: MediaSearchQuery.Medium?): Media {
        if (entity != null) {
            return Media(
                id = entity.id,
                title = entity.title?.userPreferred ?: "",
                image = entity.coverImage?.large ?: "",
                format = entity.format?.rawValue ?: "",
                isFavorite = entity.isFavourite,
                status = entity.startDate?.year.toString(),
                averageScore = entity.averageScore ?: 0,
                genre = ""
            )
        }
        return Media()
    }

    override fun mapFromModelToEntity(model: Media): MediaSearchQuery.Medium? {
        return null
    }

    fun mapFromEntityList(entities: List<MediaSearchQuery.Medium?>?): List<Media> {
        return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
    }
}