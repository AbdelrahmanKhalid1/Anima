package com.ak.anima.ui.search.results.staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.anima.model.index.Staff
import com.ak.anima.repository.StaffRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StaffViewModel @Inject constructor(private val staffRepository: StaffRepository) :
    ViewModel() {

    var searchResult: LiveData<PagingData<Staff>> = MutableLiveData()
//    var scrollPosition = 0

    fun searchStaff(query: String) {

        searchResult = staffRepository.searchStaff(query)
            .cachedIn(viewModelScope)

    }
}
