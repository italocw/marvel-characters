package com.example.marvel_characters.ui.compose.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_characters.BaseDataUiState
import com.example.marvel_characters.Result
import com.example.marvel_characters.domain.MarvelCharacter
import com.example.marvel_characters.repository.Repository
import com.example.marvel_characters.succeeded
import com.example.marvel_characters.ui.compose.CHARACTER_DETAIL_ARG_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarvelCharacterUIState(loading = true))
    val uiState: StateFlow<MarvelCharacterUIState> = _uiState
    private val characterId: String = savedStateHandle.get<String>(CHARACTER_DETAIL_ARG_KEY)!!

    init {
        viewModelScope.launch {
            fetchCharacter()
        }
    }

    private suspend fun fetchCharacter() {
        val result = repository.getSavedCharacter(characterId)
        if (result.succeeded) {
            val marvelCharacter = (result as Result.Success).data
            _uiState.value = MarvelCharacterUIState(marvelCharacter, isCharacterSaved = true)

        } else {
            fetchCharacterFromWeb()
        }
    }



    private suspend fun fetchCharacterFromWeb() {

        val result = repository.getCharacterByIdFromWeb(id = characterId)
        if (result.succeeded) {
            val marvelCharacter = (result as Result.Success).data
            _uiState.value = MarvelCharacterUIState(marvelCharacter, isCharacterSaved = false)

        } else {
            _uiState.value = MarvelCharacterUIState(
                error = (result as Result.Error).exception.message
            )
        }

    }

    fun onFavoritePressed() {
        viewModelScope.launch {
            if (!uiState.value.isCharacterSaved) {
                saveCharacter()
            } else {
                removeCharacter()
            }
        }
    }

    private suspend fun saveCharacter() {
        repository.saveCharacter(uiState.value.marvelCharacter!!)
        _uiState.value = uiState.value.copy(isCharacterSaved = true)
    }


    private suspend fun removeCharacter() {
        repository.deleteCharacter(uiState.value.marvelCharacter!!)
        _uiState.value = uiState.value.copy(isCharacterSaved = false)
    }


    data class MarvelCharacterUIState(
        val marvelCharacter: MarvelCharacter? = null,
        override val loading: Boolean = false,
        override val error: String? = null,
        val isCharacterSaved: Boolean = false
    ) :
        BaseDataUiState(loading, error)
}