package com.ak.anima.model.details

import com.ak.queries.user.CurrentUserQuery

data class AuthUser(
    val id: Int,
    val name: String,
    val avatar: String,
    val banner: String,
    val unreadNotificationCount: Int,
    val options: CurrentUserQuery.Options?,
    val mediaLIstOptions: CurrentUserQuery.MediaListOptions?,
)

//data class AuthOptions(
//    val titleLanguage: UserTitleLanguage,
//)
//data class MediaListOptions(
//    val titleLanguage:UserTitleLanguage,
//)