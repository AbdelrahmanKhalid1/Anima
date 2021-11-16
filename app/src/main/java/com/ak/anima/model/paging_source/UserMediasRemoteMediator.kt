package com.ak.anima.model.paging_source

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.ak.anima.db.AnimaDatabase
import com.ak.anima.model.index.Media
import com.ak.anima.remote.mapper.MediaMapper
import com.ak.anima.utils.EmptyDataException
import com.ak.anima.utils.Utils
import com.ak.queries.media.UserMediaListQuery
import com.ak.type.MediaListStatus
import com.ak.type.MediaType
import com.ak.type.ScoreFormat
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import retrofit2.HttpException
import java.io.IOException
import kotlin.Exception

private const val TAG = "UserMedias"

@OptIn(ExperimentalPagingApi::class)
class UserMediasRemoteMediator(
    private val apolloClient: ApolloClient,
    private val database: AnimaDatabase,
    private val mediaMapper: MediaMapper,
    private val mediaType: MediaType?,
    private val mediaListStatus: MediaListStatus?,
    private val userId: Int,
    private val scoreFormat: ScoreFormat,
    private var currentPage: Int = 1,
) : RemoteMediator<Int, Media>() {
    private val mediaDao = database.mediaDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Media>): MediatorResult =
        try {
            when (loadType) {
                LoadType.REFRESH -> {
                }
                LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> state.lastItemOrNull()
                    ?: MediatorResult.Success(endOfPaginationReached = true)
            }

            val response = apolloClient.query(
                UserMediaListQuery(
                    Input.optional(currentPage),
                    Input.optional(userId),
                    Input.optional(mediaType),
                    Input.optional(mediaListStatus),
                    Input.optional(scoreFormat)
                )
            ).await()
            val data = response.data?.page
            val mediaCached =
                mediaMapper.userMediaListCacheMapper.mapFromEntityList(data?.mediaList, mediaListStatus?.rawValue)
            Log.d(TAG, "load: $currentPage $mediaListStatus ${mediaCached.size} ${currentPage+1}")
            if (mediaCached.isEmpty() && currentPage == 1)
                throw EmptyDataException("No ${Utils.capitalizeFirstLetter(mediaListStatus?.rawValue)}" +
                        " medias were found", null)
            database.withTransaction {
//            database.mediaDao().removeListMedias()
                mediaDao.addAllMedias(mediaCached)
            }

            val hasNextPage = data?.pageInfo?.hasNextPage ?: false
            if (hasNextPage)
                currentPage++
            MediatorResult.Success(endOfPaginationReached = !hasNextPage)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: ApolloNetworkException) {
            MediatorResult.Success(endOfPaginationReached = true)
        }  catch (e: ApolloHttpException) {
            MediatorResult.Success(endOfPaginationReached = true)
        }catch (e: Exception) {
            MediatorResult.Error(e)
        }
}