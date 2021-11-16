package com.ak.anima.model.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ak.anima.model.index.User
import com.ak.anima.remote.mapper.UserSearchMapper
import com.ak.anima.utils.EmptyDataException
import com.ak.queries.user.UserSearchQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class SearchUserPaging(
    private val apolloClient: ApolloClient,
    private val query: String,
    private val userMapper: UserSearchMapper
) : PagingSource<Int, User>(){
    override fun getRefreshKey(state: PagingState<Int, User>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User>  = try{
        val currentPage = params.key ?: 1
        val response = apolloClient.query(
            UserSearchQuery(
                page = Input.optional(currentPage),
                query = Input.optional(query)
            )
        ).await()
        val data = response.data?.page
        val userList = userMapper.mapFromEntityList(data?.users)

        if(userList.isEmpty())
            throw EmptyDataException("No users founded", EmptyDataException.SearchResultThrowable(query))

        LoadResult.Page(
            data = userList,
            prevKey = if (currentPage != 1) currentPage.minus(1) else null,
            nextKey = if (data?.pageInfo?.hasNextPage!!) currentPage.plus(1) else null
        )
    }catch (ignore: IOException) { //there is no internet connection
        LoadResult.Error(ignore)
    } catch (ignore: HttpException) {//something wrong with the server
        LoadResult.Error(ignore)
    } catch (ignore: NullPointerException) {//any data is null
        LoadResult.Error(ignore)
    } catch (ignore: Exception) {
        LoadResult.Error(ignore)
    }
}
