package com.ak.otaku_kun.remote.mapper

import com.ak.otaku_kun.db.entity.MediaCacheEntity
import com.ak.otaku_kun.model.details.Anime
import com.ak.otaku_kun.model.details.Manga
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.model.index.Media as MediaIndex
import com.ak.otaku_kun.model.details.Media as MediaDetails
import com.ak.otaku_kun.utils.Mapper
import com.ak.otaku_kun.utils.Utils
import com.ak.quries.media.MediaBrowseQuery
import com.ak.quries.media.MediaQuery
import com.ak.quries.media.MediaSearchQuery
import javax.inject.Inject

class MediaMapper @Inject constructor() {
    val mediaBrowseMapper = MediaBrowseMapper()
    val mediaSearchMapper = MediaSearchMapper()
    val mediaDetailsCacheMapper = MediaDetailsCacheMapper()
    val mediaDetailsMapper = MediaDetailsMapper()
}

class MediaBrowseMapper : Mapper<MediaBrowseQuery.Medium?, MediaIndex> {

    override fun mapFromEntityToModel(entity: MediaBrowseQuery.Medium?): MediaIndex {
        if (entity == null)
            return MediaIndex()

        return entity.run {
            MediaIndex(
                id,
                title?.userPreferred ?: "",
                coverImage?.extraLarge ?: coverImage?.large ?: "",
                Utils.capitalizeFirstLetter(format?.rawValue),
                isFavourite,
                Utils.capitalizeFirstLetter(status?.rawValue),
                Utils.formatScore(averageScore),
                genres.toString().run { substring(1, length - 1) },
                MediaDetails.MediaListEntry.newInstance(mediaListEntry)
            )
        }
    }

    override fun mapFromModelToEntity(model: MediaIndex): MediaBrowseQuery.Medium? {
        return null
    }

    fun mapFromEntityList(entities: List<MediaBrowseQuery.Medium?>?): List<MediaIndex> {
        return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
    }
}

class MediaSearchMapper : Mapper<MediaSearchQuery.Medium?, MediaIndex> {

    override fun mapFromEntityToModel(entity: MediaSearchQuery.Medium?): MediaIndex {
        if (entity == null)
            return MediaIndex()

        return entity.run {
            MediaIndex(
                id = id,
                title = title?.userPreferred ?: "",
                image = coverImage?.large ?: "",
                format = Utils.capitalizeFirstLetter(format?.rawValue),
                isFavorite = isFavourite,
                status = startDate?.year.toString(),
                averageScore = Utils.formatScore(averageScore)
            )
        }
    }

    override fun mapFromModelToEntity(model: MediaIndex): MediaSearchQuery.Medium? {
        return null
    }

    fun mapFromEntityList(entities: List<MediaSearchQuery.Medium?>?): List<MediaIndex> {
        return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
    }
}

class MediaDetailsCacheMapper : Mapper<MediaCacheEntity?, MediaQuery.Medium?> {
    override fun mapFromEntityToModel(entity: MediaCacheEntity?): MediaQuery.Medium? {
        return null
    }

    override fun mapFromModelToEntity(model: MediaQuery.Medium?): MediaCacheEntity? {
        if (model == null)
            return null

        return model.run {
            MediaCacheEntity(
                id,
                type?.rawValue ?: "ANIME",
                title!!.let {
                    MediaDetails.Title.newTitle(
                        it.romaji,
                        it.english,
                        it.native_,
                        it.userPreferred
                    )
                },
                Utils.capitalizeFirstLetter(format?.rawValue),
                coverImage?.extraLarge ?: "",
                bannerImage ?: "",
                Utils.capitalizeFirstLetter(status?.rawValue),
                meanScore ?: 0,
                isFavourite,
                genres.toString().run { substring(1, length - 1) },
                Utils.formatDate(startDate, "Started"),
                Utils.formatDate(endDate, "Ended"),
                Utils.capitalizeFirstLetter(source?.rawValue),
                description ?: Const.NO_VALUE,
                "studio", //todo
                Utils.getSeason(type, season, startDate),
                Utils.setSeasonYear(seasonYear, startDate, type),
                averageScore ?: 0,
                popularity ?: 0,
                null,
                siteUrl ?: Const.NO_VALUE,
                tags?.mapNotNull { tag -> Utils.mapTag(tag!!) } ?: emptyList(),
                Utils.mapTrailer(trailer),
//                relation
                episodes?.toString() ?: Const.NO_VALUE,
                chapters?.toString() ?: Const.NO_VALUE,
                duration?.toString() ?: Const.NO_VALUE,
                volumes?.toString() ?: Const.NO_VALUE,
//            characters
//            staff
            )
        }
    }
}

class MediaDetailsMapper : Mapper<MediaCacheEntity?, MediaDetails?> {
    override fun mapFromEntityToModel(entity: MediaCacheEntity?): MediaDetails? {
        if (entity == null)
            return null

        return entity.run {
            if (type == "MANGA")
                Manga(
                    id,
                    title,
                    format,
                    cover,
                    banner,
                    status,
                    Utils.formatScore(meanScore),
                    isFavorite,
                    //year
                    genre,
                    startDate,
                    endDate,
                    source,
                    description,
                    studio,
                    season,
                    seasonYear,
                    Utils.formatScore(averageScore),
                    popularity,
                    mediaListEntry,
                    siteUrl,
                    tags,
                    trailer,
//                relation
                    emptyList(),//characters
                    emptyList(),//staff
                    chapters,
                    volumes
                )
            else
                Anime(
                    id,
                    title,
                    format,
                    cover,
                    banner,
                    status,
                    Utils.formatScore(meanScore),
                    isFavorite,
                    genre,
                    startDate,
                    endDate,
                    source,
                    description,
                    studio,
                    season,
                    seasonYear,
                    Utils.formatScore(averageScore),
                    popularity,
                    mediaListEntry,
                    siteUrl,
                    tags,
                    trailer,
//                relation
                    emptyList(),//characters
                    emptyList(),//staff
                    episodes,
                    duration,
                )
        }
    }

    override fun mapFromModelToEntity(model: MediaDetails?): MediaCacheEntity? {
        return null
    }
}