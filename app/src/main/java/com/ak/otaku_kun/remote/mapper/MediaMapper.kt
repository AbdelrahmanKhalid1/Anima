package com.ak.otaku_kun.remote.mapper

import com.ak.otaku_kun.db.entity.MediaCacheEntity
import com.ak.otaku_kun.model.details.Anime
import com.ak.otaku_kun.model.details.Manga
import com.ak.otaku_kun.model.index.*
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.Keys
import com.ak.otaku_kun.model.index.Media as MediaIndex
import com.ak.otaku_kun.model.details.Media as MediaDetails
import com.ak.otaku_kun.utils.Mapper
import com.ak.otaku_kun.utils.Utils
import com.ak.queries.media.MediaBrowseQuery
import com.ak.queries.media.MediaQuery
import com.ak.queries.media.MediaSearchQuery
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

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
                formatNextAiringEpisode(nextAiringEpisode)
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
            title = title?.userPreferred ?: Const.NO_VALUE,
            format = Utils.capitalizeFirstLetter(format?.rawValue),
            cover = coverImage?.extraLarge ?: coverImage?.large ?: "",
            averageScore = Utils.formatScore(averageScore),
            isFavorite = isFavourite,
//            mediaListEntry = null
        )
    }

    private fun formatNextAiringEpisode(nextAiringEpisode: MediaQuery.NextAiringEpisode?): String {
        if (nextAiringEpisode?.timeUntilAiring == null)
            return Const.NO_VALUE

        return nextAiringEpisode.run {
            when {
                timeUntilAiring < 60 -> { //less than 60 seconds
                    "Ep $episode: 1m"
                }
                timeUntilAiring < 3600 -> { //60*60 = 3600 sec ==> less than 1 hour
                    "Ep $episode: ${timeUntilAiring / 60 % 60}m"
                }
                timeUntilAiring < 86400 -> { //60*60*24 = 86400 sec ==> less than 1 day
                    "Ep $episode: ${timeUntilAiring / (60 * 60) % 24}h ${timeUntilAiring / 60 % 60}m"
                }
                else -> {
                    "Ep $episode: ${timeUntilAiring / (60 * 60 * 24)}d ${timeUntilAiring / (60 * 60) % 24}h ${timeUntilAiring / 60 % 60}m"
                }
            }
        }
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