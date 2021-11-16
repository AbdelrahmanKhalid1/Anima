package com.ak.anima.model.details

import com.ak.anima.db.entity.MediaCacheEntity
import com.ak.anima.model.index.Character
import com.ak.anima.model.index.Staff
import com.ak.anima.utils.Const
import com.ak.anima.model.index.Media as MediaIndex
import com.ak.anima.utils.Utils

class Anime(
    id: Int,
    title: Title,
    format: String,
    cover: String,
    banner: String,
    status: String,
    meanScore: String,
    isFavorite: Boolean,
    genre: String,
    startDate: String,
    endDate: String,
    source: String,
    description: String,
    studio: Pair<Int, String>,
    season: String,
    seasonYear: String,
    averageScore: String,
    popularity: Int,
    mediaListEntry: MediaListEntry?,
    siteUrl: String,
    tags: List<Tag>,
    trailer: Trailer?,
    relation: List<Pair<String, List<MediaIndex>>>,
    characters: List<Character>,
    staff: List<Staff>,
    val episodes: String,
    val duration: String,
    val timeUntilAiring: String,
) : Media(
    id, title, format, cover, banner, status, meanScore, isFavorite,
    genre, startDate, endDate, source, description, studio, season,
    seasonYear, averageScore, popularity, mediaListEntry, siteUrl,
    tags, trailer, relation, characters, staff
) {
    constructor(
        mediaCache: MediaCacheEntity,
        characters: List<Character>,
        staff: List<Staff>,
        episodes: String,
        duration: String,
    ) : this(
        mediaCache.id,
        mediaCache.title,
        mediaCache.format,
        mediaCache.cover,
        mediaCache.banner,
        mediaCache.status,
        Utils.formatScore(mediaCache.meanScore),
        mediaCache.isFavorite,
        mediaCache.genre,
        mediaCache.startDate,
        mediaCache.endDate,
        mediaCache.source,
        mediaCache.description,
        mediaCache.studio ?: Pair(Const.NO_ITEM, Const.NO_VALUE),
        mediaCache.season,
        mediaCache.seasonYear,
        Utils.formatScore(mediaCache.averageScore),
        mediaCache.popularity,
        mediaCache.mediaListEntry,
        mediaCache.siteUrl,
        mediaCache.tags ?: emptyList(),
        mediaCache.trailer,
        mediaCache.relations?.mapNotNull { Pair(it.key, it.value) } ?: emptyList(),
        characters,
        staff,
        episodes,
        duration,
        mediaCache.timeUntilAiring
    )
}