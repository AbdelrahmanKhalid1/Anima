package com.ak.otaku_kun.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.otaku_kun.model.converter.SearchStaffPaging
import com.ak.otaku_kun.model.converter.SearchUserPaging
import com.ak.otaku_kun.model.index.User
import com.ak.otaku_kun.remote.mapper.UserMapper
import com.ak.otaku_kun.utils.Const
import com.apollographql.apollo.ApolloClient
import javax.inject.Inject

class UserRepository @Inject constructor(private val apolloClient: ApolloClient, private val userMapper: UserMapper) {

    fun searchUsers(query: String): LiveData<PagingData<User>> = Pager(
        config = PagingConfig(
            pageSize = Const.PAGE_SIZE,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            SearchUserPaging(
                apolloClient,
                query,
                userMapper.userSearchMapper
            )
        }
    ).liveData

}
