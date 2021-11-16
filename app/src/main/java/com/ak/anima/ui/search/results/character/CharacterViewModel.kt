package com.ak.anima.ui.search.results.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.anima.model.index.Character
import com.ak.anima.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    var searchResult: LiveData<PagingData<Character>> = MutableLiveData()
//    var scrollPosition = 0

    fun getCharactersByName(query:String) {
        searchResult = characterRepository.searchCharacter(query)
            .cachedIn(viewModelScope)

    }
}
