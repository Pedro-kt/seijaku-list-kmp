package com.yumedev.seijakulistkmp.features.home.presentation.sectionlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.home.domain.model.SectionType
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetSectionMediaUseCase
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SectionListState(
    val items: List<AnimeCardItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = true
)

class SectionListViewModel(
    private val sectionType: SectionType,
    private val mediaType: MediaType,
    private val getSectionMediaUseCase: GetSectionMediaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SectionListState())
    val state: StateFlow<SectionListState> = _state.asStateFlow()

    init {
        loadInitialPage()
    }

    fun loadInitialPage() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getSectionMediaUseCase(
                sectionType = sectionType,
                mediaType = mediaType,
                page = 1
            ).onSuccess { data ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        items = data.items,
                        currentPage = data.currentPage,
                        hasNextPage = data.hasNextPage,
                        error = null
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun loadNextPage() {
        if (_state.value.isLoadingMore || !_state.value.hasNextPage) return

        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }

            val nextPage = _state.value.currentPage + 1

            getSectionMediaUseCase(
                sectionType = sectionType,
                mediaType = mediaType,
                page = nextPage
            ).onSuccess { result ->
                _state.update {
                    it.copy(
                        isLoadingMore = false,
                        items = it.items + result.items,
                        currentPage = result.currentPage,
                        hasNextPage = result.hasNextPage
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoadingMore = false,
                        error = error.message ?: "Failed to load more items"
                    )
                }
            }
        }
    }

    fun retry() {
        loadInitialPage()
    }
}
