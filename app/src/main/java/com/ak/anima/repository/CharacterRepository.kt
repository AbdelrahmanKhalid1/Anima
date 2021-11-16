package com.ak.anima.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.anima.model.paging_source.SearchCharacterPaging
import com.ak.anima.model.index.Character
import com.ak.anima.remote.mapper.CharacterMapper
import com.ak.anima.utils.Const
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