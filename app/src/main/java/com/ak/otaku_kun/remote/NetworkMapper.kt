package com.ak.otaku_kun.remote

import com.ak.MediaBrowseQuery
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.model.index.MediaListEntry
import com.ak.otaku_kun.utils.Mapper
import javax.inject.Inject

class MediaMapper @Inject constructor() : Mapper<MediaBrowseQuery.Medium?, Media>{

//    val browseAnimeMapper = BrowseAnimeMapper()
//    val browseMangaMapper = BrowseMangaMapper()

    override fun mapFromEntityToModel(entity: MediaBrowseQuery.Medium?): Media {
        if (entity != null) {
            return Media(
                entity.id,
                entity.title?.userPreferred ?: "",
                entity.coverImage?.extraLarge ?: "",
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

//    class BrowseAnimeMapper : Mapper<MediaBrowseQuery.Medium?, Anime> {
//        fun mapFromEntityList(entities: List<MediaBrowseQuery.Medium?>?): List<Anime> {
//            return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
//        }
//
//        override fun mapFromEntityToModel(entity: MediaBrowseQuery.Medium?): Anime {
//            if (entity != null) {
//                return Anime(
//                    entity.id,
//                    entity.title?.userPreferred ?: "",
//                    entity.coverImage?.extraLarge ?: "",
//                    entity.format?.rawValue ?: "",
//                    entity.isFavourite,
//                    entity.status?.rawValue ?: "",
//                    entity.averageScore ?: 0,
//                    entity.genres.toString().also { it.substring(1, it.length - 1) },
//                    MediaListEntry.newInstance(entity.mediaListEntry)
//                )
//            }
//            return Anime()
//        }
//
//        override fun mapFromModelToEntity(model: Anime): MediaBrowseQuery.Medium? {
//            return null
//        }
//    }
//
//    class BrowseMangaMapper @Inject constructor() : Mapper<MediaBrowseQuery.Medium?, Manga> {
//
//        fun mapFromEntityList(entities: List<MediaBrowseQuery.Medium?>?): List<Manga> {
//            return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
//        }
//
//        override fun mapFromEntityToModel(entity: MediaBrowseQuery.Medium?): Manga {
//            if (entity != null) {
//                return Manga(
//                    entity.id,
//                    entity.title!!.userPreferred!!,
//                    entity.coverImage?.extraLarge!!,
//                    entity.format?.rawValue!!,
//                    entity.isFavourite,
//                    entity.status?.rawValue!!,
//                    entity.averageScore!!,
//                    entity.genres.toString(),
//                    MediaListEntry.newInstance(entity.mediaListEntry)
//                )
//            }
//            return Manga()
//        }
//
//        override fun mapFromModelToEntity(model: Manga): MediaBrowseQuery.Medium? {
//            return null
//        }
//    }
}
