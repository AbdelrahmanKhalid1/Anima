package com.ak.otaku_kun.model.details

import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.model.index.MediaListEntry

class Anime(
    id: Int = -1 , title: String="", image: String="", format: String ="", isFavorite: Boolean=false, status: String="",
    averageScore: Int=0, genre: String="", mediaListEntry: MediaListEntry? = null
) : Media(id, title, image, format, isFavorite, status, averageScore, genre, mediaListEntry)