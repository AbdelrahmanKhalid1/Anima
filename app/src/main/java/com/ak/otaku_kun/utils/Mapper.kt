package com.ak.otaku_kun.utils

interface Mapper<Entity, Model> {
    fun mapFromEntityToModel(entity: Entity): Model
    fun mapFromModelToEntity(model: Model) : Entity
}