package com.ak.otaku_kun.model.remote.index

import com.ak.otaku_kun.model.Media

class Anime(
     id : Int = -1,
     title: String = "",
     image: String = ""
) : Media(id, title, image)