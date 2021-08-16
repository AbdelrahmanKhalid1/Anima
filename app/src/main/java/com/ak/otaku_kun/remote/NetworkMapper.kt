package com.ak.otaku_kun.remote

import com.ak.MediaBrowseQuery
import com.ak.otaku_kun.model.remote.details.Anime
import com.ak.otaku_kun.utils.Mapper
import javax.inject.Inject

class MediaMapper @Inject constructor() {

    abstract class BrowseAnimeMapper : Mapper<MediaBrowseQuery.Medium?, Anime> {
        fun mapFromEntityList(entities: List<MediaBrowseQuery.Medium?>?): List<Anime> {
            return entities!!.mapNotNull { mapFromEntityToModel(it) }
        }
    }
//    abstract class BrowseMangaMapper : Mapper<MediaBrowseQuery.Medium?, Anime>{
//        abstract  fun mapFromEntityList(entities: List<MediaBrowseQuery.Medium?>?): List<Anime>
//    }

    val browseAnimeMapper = object : BrowseAnimeMapper() {
        override fun mapFromEntityToModel(entity: MediaBrowseQuery.Medium?): Anime {
            if (entity != null) {
                return Anime(
                    entity.id,
                    entity.title?.userPreferred!!,
                    entity.coverImage?.extraLarge!!
                )
            }
            return Anime()
        }

        override fun mapFromModelToEntity(model: Anime): MediaBrowseQuery.Medium? {
            return null
        }
    }

//    class BrowseMangaMapper @Inject constructor() : Mapper<MediaBrowseQuery.Medium?, Manga> {
//        override fun mapFromEntityToModel(entity: MediaBrowseQuery.Medium?): Manga {
//            if (entity != null) {
//                return Manga(entity.id, entity.title!!.userPreferred!!)
//            }
//            return Manga()
//        }
//
//        override fun mapFromModelToEntity(model: Manga): MediaBrowseQuery.Medium? {
//            return null
//        }
//
//        fun mapFromEntityList(entities: List<MediaBrowseQuery.Medium>): List<Manga>{
//            return entities.map { mapFromEntityToModel(it) }
//        }
//    }
}
