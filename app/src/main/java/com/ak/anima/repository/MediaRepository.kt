package com.ak.anima.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import androidx.room.withTransaction
import com.ak.anima.db.AnimaDatabase
import com.ak.anima.model.paging_source.*
import com.ak.anima.model.index.Media as MediaIndex
import com.ak.anima.model.details.Media as MediaDetails
import com.ak.anima.model.index.Review
import com.ak.anima.remote.mapper.MediaMapper
import com.ak.anima.utils.Const
import com.ak.anima.utils.DataState
import com.ak.anima.utils.EmptyDataException
import com.ak.anima.utils.QueryFilters
import com.ak.queries.media.MediaBrowseQuery
import com.ak.queries.media.MediaQuery
import com.ak.type.*
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "MediaRepository"

class MediaRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mediaMapper: MediaMapper,
    private val database: AnimaDatabase,
) {

    private var job: CompletableJob? = null

    fun requestBrowseMedia(filters: QueryFilters): LiveData<PagingData<MediaIndex>> {
        return Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
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
    }

    suspend fun requestDiscoverMedia(
        mediaType: MediaType,
        mediaStatus: MediaStatus?,
        mediaSort: MediaSort,
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
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
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
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
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
                        if (id == null){
                            mediaLoadJob.complete()
                            return@launch
                        }

                        val mediaModel = try {
                            getMediaDetailsFromClient(id)
                        } catch (ignore: Exception) {
                            val mediaCached = database.mediaDao().getMediaForId(id)
                            mediaMapper.mediaDetailsMapper.mapFromEntityToModel(mediaCached)
                        }
                        withContext(Dispatchers.Main) {
                            value = mediaModel
                            mediaLoadJob.complete()
                        }
                    }
                }
            }
        }
    }

    private suspend fun getMediaDetailsFromClient(id: Int?): MediaDetails? {
        val response =
            apolloClient.query(MediaQuery(mediaId = Input.optional(id))).await()
        val queryMedia = response.data?.page?.media?.get(0)
        val mediaCached =
            mediaMapper.mediaDetailsCacheMapper.mapFromModelToEntity(queryMedia)
        //cache media info
        mediaCached?.let { media ->
            database.withTransaction {
                media.mediaListStatus =
                    database.mediaDao().getMediaListStatusForId(media.id)
                database.mediaDao().updateMediaCache(media)
            }
        }
        return mediaMapper.mediaDetailsMapper.mapFromEntityToModel(mediaCached)
    }

    fun cancelJobs() {
        job?.cancel()
    }

    suspend fun requestMediaCharacter(
        mediaId: Int,
        currentPage: Int,
    ): Flow<DataState<Any>> =
        flow {
            try {
                if (currentPage == 1) //first page
                    emit(DataState.Loading)

                val response = apolloClient.query(
                    MediaQuery(
                        mediaId = Input.optional(mediaId),
                        pageCharacter = Input.optional(currentPage)
                    )
                ).await()

                val responseData = response.data?.page?.media?.get(0)?.characters
                val characterList =
                    mediaMapper.mediaDetailsMapper.characterMapper.mapFromEntityList(responseData?.edges)
                if (characterList.isEmpty())
                    throw EmptyDataException("No characters for this series where found")

                emit(DataState.Success(Pair(responseData?.pageInfo?.hasNextPage ?: false,
                    characterList)))
            } catch (ignore: Exception) {
                emit(DataState.Error(ignore))
                Log.e(TAG, "requestMediaCharacter: ", ignore)
            }
        }

    suspend fun requestMediaStaff(
        mediaId: Int,
        currentPage: Int,
    ): Flow<DataState<Any>> =
        flow {
            try {
                if (currentPage == 1) //first page
                    emit(DataState.Loading)

                val response = apolloClient.query(
                    MediaQuery(
                        mediaId = Input.optional(mediaId),
                        pageStaff = Input.optional(currentPage)
                    )
                ).await()

                val responseData = response.data?.page?.media?.get(0)?.staff
                val staffList =
                    mediaMapper.mediaDetailsMapper.staffMapper.mapFromEntityList(responseData?.edges)

                if (staffList.isEmpty())
                    throw EmptyDataException("No staff for this series where found")

                emit(DataState.Success(Pair(responseData?.pageInfo?.hasNextPage ?: false,
                    staffList)))
            } catch (ignore: Exception) {
                emit(DataState.Error(ignore))
                Log.e(TAG, "requestMediaStaff: ", ignore)
            }
        }

    fun requestMediaReviews(mediaId: Int): LiveData<PagingData<Review>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MediaReviewsPaging(
                    apolloClient,
                    mediaMapper.mediaDetailsMapper.reviewMapper,
                    mediaId
                )
            }
        ).liveData

    @ExperimentalPagingApi
    fun requestUserMediaList(
        mediaType: MediaType?,
        mediaListStatus: MediaListStatus?,
        userId: Int,
        scoreFormat: ScoreFormat,
    ): LiveData<PagingData<MediaIndex>> = Pager(
        config = PagingConfig(
            pageSize = Const.PAGE_SIZE,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            enablePlaceholders = false
        ),
        remoteMediator = UserMediasRemoteMediator(
            apolloClient,
            database,
            mediaMapper,
            mediaType,
            mediaListStatus,
            userId,
            scoreFormat
        )
    ) {
        database.mediaDao().pagingSource(mediaListStatus?.rawValue, mediaType?.rawValue)
    }.liveData
}