package com.ak.otaku_kun.model.converter

import com.ak.otaku_kun.model.index.Staff
import com.ak.otaku_kun.model.index.Studio
import com.ak.otaku_kun.utils.Mapper
import com.ak.quries.staff.StaffSearchQuery
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