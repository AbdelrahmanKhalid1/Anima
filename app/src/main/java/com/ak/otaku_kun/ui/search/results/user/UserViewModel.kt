package com.ak.otaku_kun.ui.search.results.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.otaku_kun.model.index.User
import com.ak.otaku_kun.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    var searchResult: LiveData<PagingData<User>> = MutableLiveData()
//    var scrollPosition = 0

    fun searchStaff(query: String) {
        searchResult = userRepository.searchUsers(query)
            .cachedIn(viewModelScope)

    }
}
