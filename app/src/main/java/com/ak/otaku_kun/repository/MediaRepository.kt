package com.ak.otaku_kun.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.ak.otaku_kun.model.BrowseAnimePaging
import com.ak.otaku_kun.model.BrowseMangaPaging
import com.ak.otaku_kun.remote.MediaMapper
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.QueryFilters
import com.apollographql.apollo.ApolloClient
import javax.inject.Inject


class MediaRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mediaMapper: MediaMapper
) {

    fun getBrowseAnime(filters: QueryFilters) =
        Pager(
            config = PagingConfig(
                pageSize = Const.PAGE_SIZE,
                maxSize = Const.MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BrowseAnimePaging(
                    apolloClient,
                    mediaMapper.browseAnimeMapper,
                    filters,
                    filters.season
                )
            }
        ).liveData

    fun getBrowseManga(filters: QueryFilters) =
        Pager(
        config = PagingConfig(
            pageSize = Const.PAGE_SIZE,
            maxSize = Const.MAX_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            BrowseMangaPaging(
                apolloClient,
                mediaMapper.browseMangaMapper,
                filters,
                filters.season
            )
        }
    ).liveData
}