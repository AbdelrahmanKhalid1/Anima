package com.ak.otaku_kun.model.details

import com.ak.otaku_kun.db.entity.MediaCacheEntity
import com.ak.otaku_kun.model.index.Character
import com.ak.otaku_kun.model.index.Staff
import com.ak.otaku_kun.model.index.Media as MediaIndex
import com.ak.otaku_kun.utils.Utils
import okhttp3.internal.Util

class Manga(
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
    val chapters: String,
    val volumes: String
) : Media(
    id,
    title,
    format,
    cover,
    banner,
    status,
    meanScore,
    isFavorite,
    genre,
    startDate,
    endDate,
    source,
    description,
    studio,
    season,
    seasonYear,
    averageScore,
    popularity,
    mediaListEntry,
    siteUrl,
    tags,
    trailer,
    relation,
    characters,
    staff
) {
    constructor(
        mediaCache: MediaCacheEntity,
        characters: List<Character>,
        staff: List<Staff>,
        chapters: String,
        volumes: String
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
        mediaCache.studio,
        mediaCache.season,
        mediaCache.seasonYear,
        Utils.formatScore(mediaCache.averageScore),
        mediaCache.popularity,
        mediaCache.mediaListEntry,
        mediaCache.siteUrl,
        mediaCache.tags,
        mediaCache.trailer,
        mediaCache.relations.mapNotNull { Pair(it.key, it.value) },
        characters,
        staff,
        chapters,
        volumes
    )
}