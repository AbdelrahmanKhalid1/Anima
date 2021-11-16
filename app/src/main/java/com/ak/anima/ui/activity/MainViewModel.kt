package com.ak.anima.ui.activity

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.ak.anima.R
import com.ak.anima.model.details.AuthUser
import com.ak.anima.repository.UserRepository
import com.ak.anima.utils.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val authUser: AuthUser?,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _searchQuery = MutableLiveData<String?>()
    val searchQuery: LiveData<String?> get() = _searchQuery

    fun performSearch(query: String) {
        //todo empty check
        _searchQuery.value = query
    }

    fun resetQuery() {
        _searchQuery.value = null
    }

    fun loadAuthUserData(context: Context, token: String): LiveData<AuthUser?> {
        val liveData = MutableLiveData<AuthUser?>()
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        sp.edit {
            putString(Keys.KEY_TOKEN, token)
            commit()
        }
        viewModelScope.launch {
            val authUser = userRepository.getCurrentUser(token, sp)
            liveData.value = authUser
        }
        return liveData
    }

    fun isAuth(): Boolean = authUser != null
}
