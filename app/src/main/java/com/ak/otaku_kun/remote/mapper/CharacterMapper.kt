package com.ak.otaku_kun.remote.mapper

import com.ak.otaku_kun.model.index.Character
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.utils.Mapper
import com.ak.quries.character.CharacterSearchQuery
import com.ak.quries.media.MediaBrowseQuery
import javax.inject.Inject

class CharacterMapper @Inject constructor(){
    val characterSearchMapper = CharacterSearchMapper()
}

class CharacterSearchMapper : Mapper<CharacterSearchQuery.Character?, Character> {

    override fun mapFromEntityToModel(entity: CharacterSearchQuery.Character?): Character {
        return entity?.let {
            Character(
                it.id,
                "${it.name?.first} ${it.name?.last}",
                "${it.image?.large}",
                it.isFavourite,
                it.favourites ?: 0
            )
        } ?: Character()
    }

    override fun mapFromModelToEntity(model: Character): CharacterSearchQuery.Character? {
        return null
    }

    fun mapFromEntityList(entities: List<CharacterSearchQuery.Character?>?): List<Character> {
        return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
    }
}