package com.ak.anima.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.anima.model.paging_source.SearchStudioPaging
import com.ak.anima.model.index.Studio
import com.ak.anima.remote.mapper.StudioMapper
import com.ak.anima.utils.Const
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
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
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
