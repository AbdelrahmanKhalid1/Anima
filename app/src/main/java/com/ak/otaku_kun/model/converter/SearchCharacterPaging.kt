package com.ak.otaku_kun.model.converter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ak.otaku_kun.model.index.Character
import com.ak.otaku_kun.remote.mapper.CharacterSearchMapper
import com.ak.quries.character.CharacterSearchQuery
import com.ak.quries.media.MediaBrowseQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class SearchCharacterPaging(
    private val apolloClient: ApolloClient,
    private val query: String,
    private val characterSearchMapper: CharacterSearchMapper
) : PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> =
        try {
            val currentPage = if (params.key == null) 1 else params.key
            val response = apolloClient.query(
                CharacterSearchQuery(
                    page = Input.optional(currentPage),
                    query = Input.optional(query)
                )
            ).await()
            val data = response.data?.page
            val mangaList = characterSearchMapper.mapFromEntityList(data?.characters)
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