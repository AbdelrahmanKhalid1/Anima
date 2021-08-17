package com.ak.otaku_kun.remote

import com.ak.MediaBrowseQuery
import com.ak.otaku_kun.model.remote.details.Anime
import com.ak.otaku_kun.model.remote.details.Manga
import com.ak.otaku_kun.utils.Mapper
import javax.inject.Inject

class MediaMapper @Inject constructor() {

    val browseAnimeMapper = BrowseAnimeMapper()
    val browseMangaMapper = BrowseMangaMapper()

    class BrowseAnimeMapper : Mapper<MediaBrowseQuery.Medium?, Anime> {
        fun mapFromEntityList(entities: List<MediaBrowseQuery.Medium?>?): List<Anime> {
            return entities!!.mapNotNull { mapFromEntityToModel(it) }
        }

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

    class BrowseMangaMapper @Inject constructor() : Mapper<MediaBrowseQuery.Medium?, Manga> {

        fun mapFromEntityList(entities: List<MediaBrowseQuery.Medium?>?): List<Manga> {
            return entities!!.map { mapFromEntityToModel(it) }
        }

        override fun mapFromEntityToModel(entity: MediaBrowseQuery.Medium?): Manga {
            if (entity != null) {
                return Manga(
                    entity.id, entity.title!!.userPreferred!!,
                    entity.coverImage?.extraLarge!!
                )
            }
            return Manga()
        }

        override fun mapFromModelToEntity(model: Manga): MediaBrowseQuery.Medium? {
            return null
        }
    }
}
