package com.ak.anima.model.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ak.anima.model.index.Studio
import com.ak.anima.remote.mapper.StudioSearchMapper
import com.ak.anima.utils.EmptyDataException
import com.ak.queries.studio.StudioSearchQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class SearchStudioPaging(
    private val apolloClient: ApolloClient,
    private val query: String,
    private val studioMapper: StudioSearchMapper
) : PagingSource<Int, Studio>() {
    override fun getRefreshKey(state: PagingState<Int, Studio>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Studio> = try {
        val currentPage = if (params.key == null) 1 else params.key
        val response = apolloClient.query(
            StudioSearchQuery(
                page = Input.optional(currentPage),
                query = Input.optional(query)
            )
        ).await()
        val data = response.data?.page
        val studioList = studioMapper.mapFromEntityList(data?.studios)

        if(studioList.isEmpty())
            throw EmptyDataException("No studios founded", EmptyDataException.SearchResultThrowable(query))

        LoadResult.Page(
            data = studioList,
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