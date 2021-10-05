package com.ak.otaku_kun.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.otaku_kun.model.converter.BrowseMediaPaging
import com.ak.otaku_kun.model.converter.SearchMediaPaging
import com.ak.otaku_kun.model.index.Media as MediaIndex
import com.ak.otaku_kun.model.details.Media as MediaDetails
import com.ak.otaku_kun.model.converter.TrendingMediaPaging
import com.ak.otaku_kun.remote.mapper.MediaMapper
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.quries.media.MediaBrowseQuery
import com.ak.quries.media.MediaQuery
import com.ak.type.MediaSort
import com.ak.type.MediaStatus
import com.ak.type.MediaType
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import kotlinx.coroutines.*
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mediaMapper: MediaMapper
) {

    private var job: CompletableJob? = null

    fun requestBrowseMedia(filters: QueryFilters): LiveData<PagingData<MediaIndex>> =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = Const.MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BrowseMediaPaging(
                    apolloClient,
                    mediaMapper.mediaBrowseMapper,
                    filters
                )
            }
        ).liveData

    suspend fun requestDiscoverMedia(
        mediaType: MediaType,
        mediaStatus: MediaStatus?,
        mediaSort: MediaSort
    ): List<MediaIndex> {
        val response = apolloClient.query(
            MediaBrowseQuery(
                type = Input.optional(mediaType),
                status = Input.optional(mediaStatus),
                sort = Input.optional(listOf(mediaSort))
            )
        ).await()
        val page = response.data?.page
        return mediaMapper.mediaBrowseMapper.mapFromEntityList(page?.media)
    }

    fun requestTrendingMedia(mediaType: MediaType): LiveData<PagingData<MediaIndex>> =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = Const.MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TrendingMediaPaging(
                    apolloClient,
                    mediaMapper.mediaBrowseMapper,
                    mediaType
                )
            }
        ).liveData

    fun searchMedia(mediaType: MediaType, query: String): LiveData<PagingData<MediaIndex>> =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = Const.MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchMediaPaging(
                    apolloClient,
                    mediaMapper.mediaSearchMapper,
                    mediaType,
                    query
                )
            }
        ).liveData

    fun requestMediaDetails(id: Int?): LiveData<MediaDetails> {
        job = Job()
        return object : LiveData<MediaDetails>() {
            override fun onActive() {
                super.onActive()
                job?.let { mediaLoadJob ->
                    CoroutineScope(Dispatchers.IO + mediaLoadJob).launch {
                        val response =
                            apolloClient.query(MediaQuery(mediaId = Input.optional(id))).await()
                        val queryMedia = response.data?.page?.media?.get(0)
                        val mediaCached = mediaMapper.mediaDetailsCacheMapper.mapFromModelToEntity(queryMedia)
                        val mediaModel = mediaMapper.mediaDetailsMapper.mapFromEntityToModel(mediaCached)
                        withContext(Dispatchers.Main) {
                            value = mediaModel
                            mediaLoadJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        job?.cancel()
    }
}