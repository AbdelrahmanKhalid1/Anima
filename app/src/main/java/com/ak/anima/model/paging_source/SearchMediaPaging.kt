package com.ak.anima.model.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ak.anima.model.index.Media
import com.ak.anima.remote.mapper.MediaSearchMapper
import com.ak.anima.utils.EmptyDataException
import com.ak.queries.media.MediaSearchQuery
import com.ak.type.MediaType
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class SearchMediaPaging(
    private val apolloClient: ApolloClient,
    private val mediaMapper: MediaSearchMapper,
    private val mediaType: MediaType,
    private val query: String
) : PagingSource<Int, Media>() {
    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> =
        try {
            val currentPage = if (params.key == null) 1 else params.key
            val response = apolloClient.query(
                MediaSearchQuery(
                    page = Input.optional(currentPage),
                    type = Input.optional(mediaType),
                    query = Input.optional(query)
                    )
            ).await()

            val data = response.data?.page
            val mediaList = mediaMapper.mapFromEntityList(data?.media)

            if (mediaList.isEmpty())
                throw EmptyDataException(
                    "No medias founded",
                    EmptyDataException.SearchResultThrowable(query)
                )

            LoadResult.Page(
                data = mediaList,
                prevKey = if (currentPage != 1) currentPage?.minus(1) else null,
                nextKey = if (data?.pageInfo?.hasNextPage!!) currentPage?.plus(1) else null
            )
        } catch (ignore: IOException) { //there is no internet connection
            LoadResult.Error(ignore)
        } catch (ignore: HttpException) {//something wrong with the server
            LoadResult.Error(ignore)
        } catch (ignore: NullPointerException) {//any data is null
            LoadResult.Error(ignore)
        } catch (ignore: Exception) {
            LoadResult.Error(ignore)
        }
}