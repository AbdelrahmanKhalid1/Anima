package com.ak.anima.utils

interface Mapper<Entity, Model> {
    fun mapFromEntityToModel(entity: Entity): Model
    fun mapFromModelToEntity(model: Model) : Entity
}