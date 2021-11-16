package com.ak.anima.remote.mapper

import com.ak.anima.model.details.AuthUser
import com.ak.anima.model.index.User
import com.ak.anima.utils.Mapper
import com.ak.queries.user.CurrentUserQuery
import com.ak.queries.user.UserSearchQuery
import javax.inject.Inject

class UserMapper @Inject constructor() {
    val userSearchMapper = UserSearchMapper()
    val authUserMapper = AuthUserMapper()
}

class UserSearchMapper : Mapper<UserSearchQuery.User?, User> {
    override fun mapFromEntityToModel(entity: UserSearchQuery.User?): User {
        return entity?.let {
            User(
                it.id,
                it.name,
                it.avatar?.large ?: "",
                it.isFollowing ?: false
            )
        } ?: User()
    }

    override fun mapFromModelToEntity(model: User): UserSearchQuery.User? {
        return null
    }

    fun mapFromEntityList(entities: List<UserSearchQuery.User?>?): List<User> {
        return entities?.mapNotNull { mapFromEntityToModel(it) } ?: emptyList()
    }
}

class AuthUserMapper : Mapper<CurrentUserQuery.Viewer?, AuthUser?> {
    override fun mapFromEntityToModel(entity: CurrentUserQuery.Viewer?): AuthUser? {
        return entity?.run {
            AuthUser(
                id = id,
                name = name,
                avatar = avatar?.let { it.large ?: it.medium } ?: "",
                banner = bannerImage ?: "",
                unreadNotificationCount = unreadNotificationCount ?: 0,
                options = options,
                mediaLIstOptions = mediaListOptions
            )
        }
    }
    override fun mapFromModelToEntity(model: AuthUser?): CurrentUserQuery.Viewer? {
        return null
    }
}