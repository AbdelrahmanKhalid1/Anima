package com.ak.anima.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.anima.di.ApolloModule
import com.ak.anima.model.paging_source.SearchUserPaging
import com.ak.anima.model.details.AuthUser
import com.ak.anima.model.index.User
import com.ak.anima.remote.mapper.UserMapper
import com.ak.anima.utils.Const
import com.ak.anima.utils.Keys
import com.ak.queries.user.CurrentUserQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.google.gson.Gson
import javax.inject.Inject

private const val TAG = "UserRepository"

class UserRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val userMapper: UserMapper,
) {

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

    suspend fun getCurrentUser(token: String, sp: SharedPreferences): AuthUser? {
        //check if user exist in DB
        var jsonUser = sp.getString(Keys.KEY_OAUTH_USER, null)
        var authUser: AuthUser?
        val gson = Gson()
        Log.d(TAG, "getCurrentUser: $jsonUser")
        jsonUser?.let {
            authUser = gson.fromJson(jsonUser, AuthUser::class.java)
            if (authUser != null)
                return authUser
        }

        //get and cache user
        val apolloClient = ApolloModule.provideApolloClient(token)
        val response = apolloClient.query(CurrentUserQuery()).await()
        authUser = userMapper.authUserMapper.mapFromEntityToModel(response.data?.viewer)
        authUser?.let {
            jsonUser = gson.toJson(it)
            sp.edit {
                putString(Keys.KEY_OAUTH_USER, jsonUser)
                commit()
            }
            Log.d(TAG, "getCurrentUser: $jsonUser")
        }
        return authUser
    }
}
