package com.ak.otaku_kun.remote.mapper

import com.ak.otaku_kun.model.index.Staff
import com.ak.otaku_kun.model.index.User
import com.ak.otaku_kun.utils.Mapper
import com.ak.quries.staff.StaffSearchQuery
import com.ak.quries.user.UserSearchQuery
import javax.inject.Inject

class UserMapper @Inject constructor() {
    val userSearchMapper = UserSearchMapper()
}

class UserSearchMapper : Mapper<UserSearchQuery.User?, User>{
    override fun mapFromEntityToModel(entity: UserSearchQuery.User?): User {
        return entity?.let {
            User(
                it.id,
                it.name,
                it.avatar?.large?:"",
                it.isFollowing?: false
            )
        }?: User()
    }

    override fun mapFromModelToEntity(model: User): UserSearchQuery.User? {
        return null
    }

    fun mapFromEntityList(entities: List<UserSearchQuery.User?>?): List<User> {
        return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
    }
}
