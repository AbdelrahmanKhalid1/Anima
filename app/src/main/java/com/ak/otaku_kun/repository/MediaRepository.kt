package com.ak.otaku_kun.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.MediaBrowseQuery
import com.ak.otaku_kun.model.converter.BrowseAnimePaging
import com.ak.otaku_kun.model.converter.BrowseMangaPaging
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.remote.MediaMapper
import com.ak.otaku_kun.model.converter.TrendingMediaPaging
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.type.MediaSort
import com.ak.type.MediaStatus
import com.ak.type.MediaType
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import javax.inject.Inject


class MediaRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mediaMapper: MediaMapper
) {

    fun requestBrowseAnime(filters: QueryFilters): LiveData<PagingData<Media>> =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = Const.MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BrowseAnimePaging(
                    apolloClient,
                    mediaMapper,
                    filters
                )
            }
        ).liveData

    fun requestBrowseManga(filters: QueryFilters) =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = Const.MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BrowseMangaPaging(
                    apolloClient,
                    mediaMapper,
                    filters
                )
            }
        ).liveData

    suspend fun requestDiscoverMedia(
        mediaType: MediaType,
        mediaStatus: MediaStatus?,
        mediaSort: MediaSort
    ): List<Media> {
        val response = apolloClient.query(
            MediaBrowseQuery(
                type = Input.optional(mediaType),
                status = Input.optional(mediaStatus),
                sort = Input.optional(listOf(mediaSort))
            )
        ).await()
        val page = response.data?.page
        return mediaMapper.mapFromEntityList(page?.media)
    }

    fun requestTrendingMedia(mediaType: MediaType): LiveData<PagingData<Media>> =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = Const.MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TrendingMediaPaging(
                    apolloClient,
                    mediaMapper,
                    mediaType
                )
            }
        ).liveData
}