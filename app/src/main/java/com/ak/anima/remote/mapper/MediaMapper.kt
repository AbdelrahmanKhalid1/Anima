package com.ak.anima.remote.mapper

import com.ak.anima.db.entity.MediaCacheEntity
import com.ak.anima.model.details.Anime
import com.ak.anima.model.details.Manga
import com.ak.anima.model.index.*
import com.ak.anima.utils.Const
import com.ak.anima.utils.Keys
import com.ak.anima.model.index.Media as MediaIndex
import com.ak.anima.model.details.Media as MediaDetails
import com.ak.anima.utils.Mapper
import com.ak.anima.utils.Utils
import com.ak.queries.media.MediaBrowseQuery
import com.ak.queries.media.MediaQuery
import com.ak.queries.media.MediaSearchQuery
import com.ak.queries.media.UserMediaListQuery
import com.ak.type.MediaStatus
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import com.ak.anima.model.details.Media.Title as MediaTitle

class MediaMapper @Inject constructor() {
    val mediaBrowseMapper = MediaBrowseMapper()
    val mediaSearchMapper = MediaSearchMapper()
    val mediaDetailsCacheMapper = MediaDetailsCacheMapper()
    val mediaDetailsMapper = MediaDetailsMapper()
    val userMediaListCacheMapper = UserMediaListCacheMapper()
}

class MediaBrowseMapper : Mapper<MediaBrowseQuery.Medium?, MediaIndex> {

    override fun mapFromEntityToModel(entity: MediaBrowseQuery.Medium?): MediaIndex {
        if (entity == null)
            return MediaIndex()

        return entity.run {
            MediaIndex(
                id,
                MediaTitle(userPreferred = title?.userPreferred!!),
//                title?.userPreferred ?: "",
                coverImage?.extraLarge ?: coverImage?.large ?: "",
                Utils.capitalizeFirstLetter(format?.rawValue),
                isFavourite,
                Utils.capitalizeFirstLetter(status?.rawValue),
                Utils.formatScore(averageScore),
                genres.toString().run { substring(1, length - 1) },
                mediaListEntry?.let {MediaDetails.MediaListEntry(it)}
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
                title = MediaTitle(userPreferred = title?.userPreferred!!),
                cover = coverImage?.large ?: "",
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
                    MediaDetails.Title(
                        it.romaji ?: Const.NO_VALUE,
                        it.english ?: Const.NO_VALUE,
                        it.native_ ?: Const.NO_VALUE,
                        it.userPreferred ?: Const.NO_VALUE
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
                mapToStudio(studios),
                Utils.getSeason(type, season, startDate),
                Utils.setSeasonYear(seasonYear, startDate, type),
                averageScore ?: 0,
                popularity ?: 0,
                null,
                siteUrl ?: Const.NO_VALUE,
                mapTag(tags),
                mapTrailer(trailer),
                mapMediaRelationToMediaIndex(relations),
                episodes?.toString() ?: Const.NO_VALUE,
                chapters?.toString() ?: Const.NO_VALUE,
                duration?.toString() ?: Const.NO_VALUE,
                volumes?.toString() ?: Const.NO_VALUE,
                nextAiringEpisode?.let {
                    Utils.formatNextAiringEpisode(it.timeUntilAiring, it.episode)
                } ?: if (status == MediaStatus.FINISHED) "" else Const.NO_VALUE
            )
        }
    }

    private fun mapToStudio(studios: MediaQuery.Studios?): Pair<Int, String> {
        if (studios?.nodes == null || studios.nodes.isEmpty())
            return Pair(Const.NO_ITEM, Const.NO_VALUE)

        return studios.run {
            Pair(
                nodes?.get(0)?.id!!,
                nodes[0]?.name!!
            )
        }
    }

    private fun mapTag(tags: List<MediaQuery.Tag?>?): List<MediaDetails.Tag> =
        tags?.mapNotNull { tag ->
            tag?.run {
                MediaDetails.Tag(
                    id = id,
                    name = name,
                    rank = rank ?: Const.NO_ITEM,
                    description = description ?: "No Description"
                )
            }
        } ?: emptyList()

    private fun mapTrailer(trailer: MediaQuery.Trailer?): MediaDetails.Trailer? {
        if (trailer == null)
            return null
        return MediaDetails.Trailer(trailer.id!!, trailer.site!!, trailer.thumbnail ?: "")
    }

    private fun mapMediaRelationToMediaIndex(relation: MediaQuery.Relations?): Map<String, List<MediaIndex>> {
        if (relation?.edges == null)
            return emptyMap()

        val hashMap = HashMap<String, ArrayList<MediaIndex>>()
        var key: String?
        relation.edges.forEach { edge ->
            edge?.run {
                key = relationType?.rawValue
                if (hashMap.containsKey(key)) {
                    node?.let { hashMap[key]?.add(mapNodeToMedia(it)) }
                } else {
                    val mediaList = ArrayList<MediaIndex>()
                    node?.let { mediaList.add(mapNodeToMedia(it)) }
                    hashMap[key!!] = mediaList
                }
            }
        }

        return hashMap.mapNotNull { Pair(Utils.capitalizeFirstLetter(it.key), it.value) }.toMap()
    }

    private fun mapNodeToMedia(node: MediaQuery.Node2): MediaIndex = node.run {
        MediaIndex(
            id = id,
//            title = title?.userPreferred ?: Const.NO_VALUE,
            title = MediaTitle(userPreferred = title?.userPreferred!!),
            format = Utils.capitalizeFirstLetter(format?.rawValue),
            cover = coverImage?.extraLarge ?: coverImage?.large ?: "",
            averageScore = Utils.formatScore(averageScore),
            isFavorite = isFavourite,
//            mediaListEntry = null
        )
    }
}

class MediaDetailsMapper : Mapper<MediaCacheEntity?, MediaDetails?> {
    val characterMapper = MediaCharacterMapper()
    val staffMapper = MediaStaffMapper()
    val reviewMapper = MediaReviewMapper()
    override fun mapFromEntityToModel(entity: MediaCacheEntity?): MediaDetails? {
        if (entity == null)
            return null

        return entity.run {
            if (type == "MANGA")
                Manga(
                    mediaCache = this,
                    characters = emptyList(),
                    staff = emptyList(),
                    chapters = chapters,
                    volumes = volumes
                )
            else
                Anime(
                    mediaCache = this,
                    characters = emptyList(),
                    staff = emptyList(),
                    episodes = episodes,
                    duration = duration
                )
        }
    }

    override fun mapFromModelToEntity(model: MediaDetails?): MediaCacheEntity? {
        return null
    }

    class MediaCharacterMapper : Mapper<MediaQuery.Node1?, Character?> {
        override fun mapFromEntityToModel(entity: MediaQuery.Node1?): Character? =
            entity?.run {
                Character(
                    id,
                    name?.run { "${first ?: ""} ${last ?: ""}" } ?: "",
                    image?.large ?: ""
                )
            }

        override fun mapFromModelToEntity(model: Character?): MediaQuery.Node1? {
            return null
        }

        fun mapFromEntityList(edges: List<MediaQuery.Edge1?>?): List<Character> {
            val set = HashSet<String>()
            val characterList = ArrayList<Character>()
            var i = 0
            edges?.let { edge ->
                while (i <= edge.lastIndex) {
                    edge[i]?.run {
                        val characterRole = Utils.capitalizeFirstLetter(role?.rawValue)
                        if (set.contains(characterRole)) {
                            val character = mapFromEntityToModel(node)
                            character?.role = characterRole
                            character?.let { characterList.add(it) }
                            i++
                        } else {
                            set.add(characterRole)
                            characterList.add(Character(id = Keys.RECYCLER_TYPE_HEADER,
                                role = characterRole))
                        }
                    }
                }
            }
            return characterList
        }
    }

    class MediaStaffMapper : Mapper<MediaQuery.Node3?, Staff?> {
        override fun mapFromEntityToModel(entity: MediaQuery.Node3?): Staff? =
            entity?.run {
                Staff(
                    id = id,
                    name = name?.run { "${first ?: ""} ${last ?: ""}" } ?: "",
                    image = image?.large ?: "",
                    language = languageV2 ?: Const.NO_VALUE,
                )
            }

        override fun mapFromModelToEntity(model: Staff?): MediaQuery.Node3? {
            return null
        }

        fun mapFromEntityList(edges: List<MediaQuery.Edge3?>?): List<Staff> {
            val set = HashSet<String>()
            val staffList = ArrayList<Staff>()
            var i = 0
            edges?.let { edge ->
                while (i <= edge.lastIndex) {
                    edge[i]?.run {
                        val staffRole = Utils.capitalizeFirstLetter(role)
                        if (set.contains(staffRole)) {
                            val staff = mapFromEntityToModel(node)
                            staff?.role = staffRole
                            staff?.let { staffList.add(it) }
                            i++
                        } else {
                            set.add(staffRole)
                            staffList.add(Staff(id = Keys.RECYCLER_TYPE_HEADER,
                                role = staffRole))
                        }
                    }
                }
            }
            return staffList
        }
    }

    class MediaReviewMapper : Mapper<MediaQuery.Edge4?, Review?> {
        override fun mapFromEntityToModel(entity: MediaQuery.Edge4?): Review? =
            entity?.node?.run {
                val upVotes = rating ?: 0
                val downVotes = ratingAmount?.let { total -> total - upVotes } ?: 0
                val vote =
                    userRating?.rawValue?.let { ReviewVote.valueOf(it) } ?: ReviewVote.NO_VOTE
                val userHolder = user?.run {
                    User(name = name, avatar = avatar?.large ?: avatar?.medium ?: "")
                } ?: User()
                Review(
                    id,
                    summary!!,
                    SimpleDateFormat("MMM dd, yyyy",
                        Locale.getDefault()).format(Date(createdAt * 1000L)),
                    score ?: 0,
                    upVotes,
                    downVotes,
                    vote,
                    userHolder
                )
            }


        override fun mapFromModelToEntity(model: Review?): MediaQuery.Edge4? {
            return null
        }

        fun mapFromEntityList(review: List<MediaQuery.Edge4?>?): List<Review> {
            return review?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
        }
    }
}

class UserMediaListCacheMapper : Mapper<UserMediaListQuery.MediaList?, MediaCacheEntity?> {
    override fun mapFromEntityToModel(entity: UserMediaListQuery.MediaList?): MediaCacheEntity? {
        if (entity == null)
            return null

        return entity.media?.run {
            MediaCacheEntity(
                id = id,
                type = type?.rawValue ?: "ANIME",
                title = title!!.let {
                    MediaDetails.Title(userPreferred = it.userPreferred ?: Const.NO_VALUE)
                },
                format = Utils.capitalizeFirstLetter(format?.rawValue),
                cover = coverImage?.extraLarge ?: coverImage?.large ?: "",
                status = Utils.capitalizeFirstLetter(status?.rawValue),
                isFavorite = isFavourite,
                timeUntilAiring = nextAiringEpisode?.let {
                    Utils.formatNextAiringEpisode(it.timeUntilAiring, it.episode)
                } ?: if (status == MediaStatus.FINISHED) "" else Const.NO_VALUE,
                mediaListEntry = com.ak.anima.model.details.Media.MediaListEntry(entity.id,
                    entity.progress ?: 0, Utils.capitalizeFirstLetter(entity.status?.rawValue),
                    entity.scoreRaw ?: 0.0,
                    entity.startedAt?.run { mapDate(day, month, year) },
                    entity.completedAt?.run { mapDate(day, month, year) },
                    entity.progressVolumes ?: 0
                ),
            )
        }
    }

    private fun mapDate(
        day: Int?,
        month: Int?,
        year: Int?,
    ): com.ak.anima.model.details.Media.Date? {
        return if (day == null || month == null || year == null)
            null
        else com.ak.anima.model.details.Media.Date(day, month, year)
    }

    override fun mapFromModelToEntity(model: MediaCacheEntity?): UserMediaListQuery.MediaList? {
        return null
    }

    fun mapFromEntityList(
        entities: List<UserMediaListQuery.MediaList?>?,
        mediaListStatus: String?,
    ): List<MediaCacheEntity> {
        return entities?.mapNotNull {
            val media = mapFromEntityToModel(it)
            media?.mediaListStatus = mediaListStatus
            media
        } ?: emptyList()
    }
}