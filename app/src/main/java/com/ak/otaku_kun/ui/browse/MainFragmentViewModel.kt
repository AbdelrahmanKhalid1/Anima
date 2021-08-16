package com.ak.otaku_kun.ui.browse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ak.otaku_kun.R
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor() : ViewModel() {

    var selectedNavItem = R.id.nav_discover
    var queryFilters = QueryFilters()

    fun getNewSelectedNavAfterFilter(): Int {
        selectedNavItem = if (queryFilters.type == MediaType.MANGA) R.id.nav_browse_manga else R.id.nav_browse_anime
        return selectedNavItem
    }

}