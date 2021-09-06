package com.ak.otaku_kun.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.otaku_kun.model.converter.SearchCharacterPaging
import com.ak.otaku_kun.model.converter.SearchStudioPaging
import com.ak.otaku_kun.model.index.Character
import com.ak.otaku_kun.model.index.Studio
import com.ak.otaku_kun.remote.mapper.CharacterMapper
import com.ak.otaku_kun.remote.mapper.StudioMapper
import com.ak.otaku_kun.utils.Const
import com.apollographql.apollo.ApolloClient
import javax.inject.Inject


class StudioRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val studioMapper: StudioMapper
) {

    fun searchStudio(query: String): LiveData<PagingData<Studio>> =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = Const.MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchStudioPaging(
                    apolloClient,
                    query,
                    studioMapper.studioSearchMapper
                )
            }
        ).liveData
}
