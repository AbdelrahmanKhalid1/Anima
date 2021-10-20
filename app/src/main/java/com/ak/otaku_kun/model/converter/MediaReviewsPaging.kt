package com.ak.otaku_kun.model.converter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ak.otaku_kun.model.index.Review
import com.ak.otaku_kun.remote.mapper.MediaDetailsMapper
import com.ak.otaku_kun.utils.EmptyDataException
import com.ak.queries.media.MediaQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "MediaReviewsPaging"
class MediaReviewsPaging(
    private val apolloClient: ApolloClient,
    private val reviewMapper: MediaDetailsMapper.MediaReviewMapper,
    private val mediaId: Int
) : PagingSource<Int, Review>() {
    override fun getRefreshKey(state: PagingState<Int, Review>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> =
        try {
            val currentPage = if (params.key == null) 1 else params.key
            Log.d(TAG, "load: $mediaId")

            val response = apolloClient.query(MediaQuery(
                mediaId = Input.optional(mediaId),
                pageCharacter = Input.optional(currentPage)
            )).await()
            val responseData = response.data?.page?.media?.get(0)?.reviews
            val reviewList = reviewMapper.mapFromEntityList(responseData?.edges)

            if (reviewList.isEmpty())
                throw EmptyDataException(
                    "No reviews for this media",
                    EmptyDataException.MediaFilterThrowable()
                )

            Log.d(TAG, "load: ${responseData?.pageInfo?.hasNextPage} ${responseData?.pageInfo?.currentPage}")
            LoadResult.Page(
                data = reviewList,
                prevKey = if (currentPage != 1) currentPage?.minus(1) else null,
                nextKey = if (responseData?.pageInfo?.hasNextPage!!) currentPage?.plus(1) else null
            )
        } catch (ignore: IOException) { //there is no internet connection
            Log.d(TAG, "load: IOException")
            LoadResult.Error(ignore)
        } catch (ignore: HttpException) {//something wrong with the server
            Log.d(TAG, "load: HttpException")
            LoadResult.Error(ignore)
        } catch (ignore: NullPointerException) {//any data is null
            Log.d(TAG, "load: NullPointerException")
            LoadResult.Error(ignore)
        } catch (ignore: Exception) {//any data is null
            Log.d(TAG, "load: NullPointerException")
            LoadResult.Error(ignore)
        }
}