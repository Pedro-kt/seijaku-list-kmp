package com.yumedev.seijakulistkmp.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.EnsureLocalProfileExistsUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.GetCurrentProfileUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.UpdateProfileStatisticsUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.UpdateProfileUseCase
import com.yumedev.seijakulistkmp.features.profile.presentation.model.ProfileError
import com.yumedev.seijakulistkmp.features.profile.presentation.model.ProfileUiState
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetListStatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val updateProfile: UpdateProfileUseCase,
    private val updateStatistics: UpdateProfileStatisticsUseCase,
    private val ensureLocalProfileExists: EnsureLocalProfileExistsUseCase,
    private val getListStats: GetListStatsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
        loadStats()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (ensureLocalProfileExists()) {
                is Result.Success -> {
                    getCurrentProfile().collect { profile ->
                        _uiState.update {
                            it.copy(
                                profile = profile,
                                isLoading = false,
                                error = null,
                            )
                        }
                    }
                }

                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = ProfileError.LoadingError,
                        )
                    }
                }
            }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            getListStats(MediaType.ANIME).collect { stats ->
                _uiState.update { it.copy(animeStats = stats) }
            }
        }
        viewModelScope.launch {
            getListStats(MediaType.MANGA).collect { stats ->
                _uiState.update { it.copy(mangaStats = stats) }
            }
        }
    }

    fun onTabChanged(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun onEditProfile() {
        _uiState.update { it.copy(isEditing = true) }
    }

    fun onCancelEdit() {
        _uiState.update { it.copy(isEditing = false) }
    }

    fun onSaveProfile(
        name: String,
        about: String?,
        avatarUrl: String?,
        bannerUrl: String?,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (
                updateProfile(
                    name = name,
                    about = about,
                    avatarUrl = avatarUrl,
                    bannerUrl = bannerUrl,
                )
            ) {
                is Result.Success -> {
                    updateStatistics()
                    _uiState.update {
                        it.copy(
                            isEditing = false,
                            isLoading = false,
                            error = null,
                        )
                    }
                }

                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = ProfileError.SavingError,
                        )
                    }
                }
            }
        }
    }

    fun onRefreshStatistics() {
        viewModelScope.launch {
            when (updateStatistics()) {
                is Result.Success -> {
                }

                is Result.Failure -> {
                    _uiState.update {
                        it.copy(error = ProfileError.UpdatingStatsError)
                    }
                }
            }
        }
    }

    fun onSyncWithAnilist() {
        // TODO: Implement when Anilist authentication is ready
        _uiState.update { it.copy(error = ProfileError.AnilistSyncUnavailable) }
    }
}
