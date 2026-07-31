package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.common.resource.Resource
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetFeaturedAnimeUseCase
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.toFeaturedMediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getFeaturedAnimeUseCase: GetFeaturedAnimeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadFeaturedAnime()
    }

    private fun loadFeaturedAnime() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingAnime = true, animeError = null) }

            getFeaturedAnimeUseCase(limit = 5).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val featuredItems = resource.data.map { dto ->
                            dto.toFeaturedMediaItem()
                        }

                        _state.update {
                            it.copy(
                                featuredAnime = featuredItems,
                                isLoadingAnime = false,
                                animeError = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoadingAnime = false,
                                animeError = resource.message
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update { it.copy(isLoadingAnime = true) }
                    }

                    is Resource.Idle -> {
                        // Do nothing, initial state
                    }
                }
            }
        }
    }

    fun retryLoadFeaturedAnime() {
        loadFeaturedAnime()
    }
}
