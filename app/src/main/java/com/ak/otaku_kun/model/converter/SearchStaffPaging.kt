package com.ak.otaku_kun.model.converter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ak.otaku_kun.model.index.Staff
import com.ak.quries.staff.StaffSearchQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

private const val TAG = "SearchStaffPaging"
class SearchStaffPaging(
    private val apolloClient: ApolloClient,
    private val query: String,
    private val staffMapper: StaffSearchMapper
) :PagingSource<Int, Staff>(){
    override fun getRefreshKey(state: PagingState<Int, Staff>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Staff> = try{
        val currentPage = params.key ?: 1
        val response = apolloClient.query(
            StaffSearchQuery(
                page = Input.optional(currentPage),
                query = Input.optional(query)
            )
        ).await()
        val data = response.data?.page
        val staffList = staffMapper.mapFromEntityList(data?.staff)
        Log.d(TAG, "load: ${staffList.size}")
        LoadResult.Page(
            data = staffList,
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
