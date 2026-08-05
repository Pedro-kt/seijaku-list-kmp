package com.yumedev.seijakulistkmp.features.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetAnimeDetailUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetMangaDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val getMangaDetailUseCase: GetMangaDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    fun loadMediaDetail(id: Int, type: MediaType) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = when (type) {
                MediaType.ANIME -> getAnimeDetailUseCase(id)
                MediaType.MANGA -> getMangaDetailUseCase(id)
            }

            result.onSuccess { mediaDetail ->
                _state.update {
                    it.copy(
                        mediaDetail = mediaDetail,
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

    fun retry(id: Int, type: MediaType) {
        loadMediaDetail(id, type)
    }

    fun toggleFavorite() {
        _state.update { currentState ->
            currentState.copy(
                mediaDetail = currentState.mediaDetail?.copy(
                    isFavorite = !currentState.mediaDetail.isFavorite
                )
            )
        }
    }

    fun addToList() {
        _state.update { currentState ->
            currentState.copy(
                mediaDetail = currentState.mediaDetail?.copy(
                    isInList = true
                )
            )
        }
    }
}
