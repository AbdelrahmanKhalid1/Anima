package com.ak.otaku_kun.model.converter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ak.otaku_kun.model.index.Media
import com.ak.otaku_kun.remote.mapper.MediaBrowseMapper
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.quries.media.MediaBrowseQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

private const val TAG = "BrowseMangaPaging"

class BrowseMediaPaging(
    private val apolloClient: ApolloClient,
    private val mediaBrowseMapper: MediaBrowseMapper,
    private val filters: QueryFilters
) : PagingSource<Int, Media>() {

    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> =
        try {
            val currentPage = if (params.key == null) 1 else params.key
            Log.d(TAG, "load: $currentPage")
            val response = apolloClient.query(
                MediaBrowseQuery(
                    page = Input.optional(currentPage),
                    type = Input.optional(filters.type),
                    format = Input.optional(filters.format),
                    status = Input.optional(filters.status),
                    season = Input.optional(filters.season),
                    seasonYear = Input.optional(filters.seasonYear),
                    startDate = Input.optional(filters.startDate),
                    source = Input.optional(filters.source),
                    genres = Input.optional(filters.listGenre),
                    sort = Input.optional(filters.listSort)
                )
            ).await()
            val data = response.data?.page
            val mangaList = mediaBrowseMapper.mapFromEntityList(data?.media)
            LoadResult.Page(
                data = mangaList,
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