package com.ak.anima.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.anima.model.paging_source.SearchStaffPaging
import com.ak.anima.remote.mapper.StaffMapper
import com.ak.anima.model.index.Staff
import com.ak.anima.utils.Const
import com.apollographql.apollo.ApolloClient
import javax.inject.Inject

class StaffRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val staffMapper: StaffMapper
) {

    fun searchStaff(query: String): LiveData<PagingData<Staff>> =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchStaffPaging(
                    apolloClient,
                    query,
                    staffMapper.staffSearchMapper
                )
            }
        ).liveData

}
