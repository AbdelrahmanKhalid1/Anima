package com.ak.anima.ui.search.results.studio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.anima.model.index.Studio
import com.ak.anima.repository.StudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudioViewModel @Inject constructor(private val studioRepository: StudioRepository) :
    ViewModel() {

    var searchResult: LiveData<PagingData<Studio>> = MutableLiveData()
//    var scrollPosition = 0

    fun searchStudio(query: String) {
        searchResult = studioRepository.searchStudio(query)
            .cachedIn(viewModelScope)

    }
}
