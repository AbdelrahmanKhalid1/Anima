package com.ak.otaku_kun.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.otaku_kun.model.converter.SearchCharacterPaging
import com.ak.otaku_kun.model.index.Character
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.remote.mapper.CharacterMapper
import com.ak.otaku_kun.utils.Const
import com.apollographql.apollo.ApolloClient
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val characterMapper: CharacterMapper
) {

    fun searchCharacter(query: String): LiveData<PagingData<Character>> =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchCharacterPaging(
                    apolloClient,
                    query,
                    characterMapper.characterSearchMapper
                )
            }
        ).liveData
}