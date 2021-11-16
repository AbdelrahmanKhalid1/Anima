package com.ak.anima.utils

import androidx.annotation.IntDef
import com.ak.anima.BuildConfig

object Keys {
    const val BASE_URL = "https://graphql.anilist.co/"
    const val AUTH_URL = "https://anilist.co/api/v2/oauth/authorize?client_id=${BuildConfig.CLIENT_ID}&response_type=token"

    //arguments "params" keys
    const val KEY_MEDIA_TYPE = "mediaType"
    const val KEY_QUERY = "query"
    const val KEY_FILTER = "filter"
    const val KEY_ITEM_ID = "itemId"
    const val KEY_MEDIA_TRAILER = "mediaTrailer"
    const val KEY_MEDIA_LIST_STATUS= "mediaTrailer"

    //states keys for saving ui state
    const val STATE_NAV_ITEM = "selectedNavItem"
    const val STATE_SEARCH_QUERY = "searchQuery"
    const val STATE_VIEW_PAGER_POSITION = "viewPagerPosition"
    const val STATE_RECYCLER_POSITION = "recyclerVisPosition"
    const val STATE_QUERY_FILTERS_BEFORE = "filterBefore"
    const val STATE_QUERY_FILTERS_AFTER = "filterAfter"
    const val STATE_PARENT_FRAGMENT_LISTENER = "parentFragmentListener"
    const val STATE_GENRE = "genreList"
    const val STATE_PARENT_FRAGMENT_LISTENER_GENRE = "viewPagerPosition"

    const val RECYCLER_TYPE_HEADER = -1
    const val RECYCLER_TYPE_CONTENT = -2
//    const val RECYCLER_TYPE_HEADER = -1

    @IntDef(RECYCLER_TYPE_HEADER, RECYCLER_TYPE_CONTENT)
    annotation class RecyclerViewType

    const val KEY_TOKEN = "user_token"
    const val KEY_OAUTH_USER = "OAuth_user_token"

}
