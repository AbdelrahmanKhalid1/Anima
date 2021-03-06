package com.ak.anima.remote.mapper

import com.ak.anima.model.index.Studio
import com.ak.anima.utils.Mapper
import com.ak.queries.studio.StudioSearchQuery
import javax.inject.Inject

class StudioMapper @Inject constructor() {
    val studioSearchMapper = StudioSearchMapper()
}

class StudioSearchMapper : Mapper<StudioSearchQuery.Studio?, Studio> {

    override fun mapFromEntityToModel(entity: StudioSearchQuery.Studio?): Studio {
        return entity?.let {
            Studio(
                it.id,
                it.name,
            )
        } ?: Studio()
    }

    override fun mapFromModelToEntity(model: Studio): StudioSearchQuery.Studio? {
        return null
    }

    fun mapFromEntityList(entities: List<StudioSearchQuery.Studio?>?): List<Studio> {
        return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
    }
}
