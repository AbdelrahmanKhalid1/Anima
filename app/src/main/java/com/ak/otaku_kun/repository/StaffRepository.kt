package com.ak.otaku_kun.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.otaku_kun.model.converter.SearchStaffPaging
import com.ak.otaku_kun.model.converter.StaffMapper
import com.ak.otaku_kun.model.index.Staff
import com.ak.otaku_kun.utils.Const
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
                maxSize = Const.MAX_PAGE_SIZE,
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
