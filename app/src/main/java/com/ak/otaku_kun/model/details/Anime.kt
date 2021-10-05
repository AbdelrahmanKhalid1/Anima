package com.ak.otaku_kun.model.details

import com.ak.otaku_kun.model.index.Character
import com.ak.otaku_kun.model.index.Staff

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
    studio: String,
    season: String,
    seasonYear: String,
    averageScore: String,
    popularity: Int,
    mediaListEntry: MediaListEntry?,
    siteUrl: String,
    tags: List<Tag>,
    trailer: Trailer?,
    characters: List<Character>,
    staff: List<Staff>,
    val episodes: String,
    val duration: String,
//    val nextEpisode: Int?
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
    characters,
    staff
)