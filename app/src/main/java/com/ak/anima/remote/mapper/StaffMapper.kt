package com.ak.anima.remote.mapper

import com.ak.anima.model.index.Staff
import com.ak.anima.utils.Mapper
import com.ak.queries.staff.StaffSearchQuery
import javax.inject.Inject

class StaffMapper @Inject constructor(){
    val staffSearchMapper = StaffSearchMapper()
}

class StaffSearchMapper : Mapper<StaffSearchQuery.Staff?, Staff> {

    override fun mapFromEntityToModel(entity: StaffSearchQuery.Staff?): Staff {
        return entity?.let {
            Staff(
                it.id,
                it.name?.full?: "",
                it.languageV2 ?: "",
                it.image?.large ?: ""
            )
        } ?: Staff()
    }

    override fun mapFromModelToEntity(model: Staff): StaffSearchQuery.Staff? {
        return null
    }

    fun mapFromEntityList(entities: List<StaffSearchQuery.Staff?>?): List<Staff> {
        return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
    }
}