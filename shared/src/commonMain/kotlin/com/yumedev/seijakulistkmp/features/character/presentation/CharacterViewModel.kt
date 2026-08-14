package com.yumedev.seijakulistkmp.features.character.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.features.character.domain.usecase.GetCharacterDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterState())
    val state: StateFlow<CharacterState> = _state.asStateFlow()

    fun loadCharacterDetail(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = getCharacterDetailUseCase(id)

            result.onSuccess { characterDetail ->
                _state.update {
                    it.copy(
                        characterDetail = characterDetail,
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { exception ->
                val errorType = ErrorMapper.mapToErrorType(exception)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = errorType
                    )
                }
            }
        }
    }

    fun retry(id: Int) {
        loadCharacterDetail(id)
    }

    fun toggleFavorite() {
        _state.update { currentState ->
            currentState.copy(
                characterDetail = currentState.characterDetail?.copy(
                    isFavourite = !currentState.characterDetail.isFavourite
                )
            )
        }
    }
}
