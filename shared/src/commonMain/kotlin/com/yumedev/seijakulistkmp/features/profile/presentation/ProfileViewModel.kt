package com.yumedev.seijakulistkmp.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.usecase.GetCurrentUserUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.EnsureLocalProfileExistsUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.GetCurrentProfileUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.UpdateProfileStatisticsUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.UpdateProfileUseCase
import com.yumedev.seijakulistkmp.features.profile.presentation.model.ProfileError
import com.yumedev.seijakulistkmp.features.profile.presentation.model.ProfileUiState
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetFavoriteMediaUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetListStatsUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.RemoveFavoriteUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ReorderFavoritesUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.SetMediaAsFavoriteUseCase
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
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getFavoriteMedia: GetFavoriteMediaUseCase,
    private val setMediaAsFavorite: SetMediaAsFavoriteUseCase,
    private val removeFavorite: RemoveFavoriteUseCase,
    private val reorderFavorites: ReorderFavoritesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
        loadStats()
        loadFavorites()
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            getCurrentUser().collect { authUser ->
                _uiState.update {
                    it.copy(isAuthenticated = authUser != null && !authUser.isAnonymous)
                }
            }
        }
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

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoriteMedia.observe(MediaType.ANIME).collect { favorites ->
                _uiState.update { it.copy(favoriteAnime = favorites) }
            }
        }
        viewModelScope.launch {
            getFavoriteMedia.observe(MediaType.MANGA).collect { favorites ->
                _uiState.update { it.copy(favoriteManga = favorites) }
            }
        }
    }

    fun onTabChanged(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun onEditProfile() {
        _uiState.update { it.copy(showEditDialog = true) }
    }

    fun onDismissEditDialog() {
        _uiState.update { it.copy(showEditDialog = false) }
    }

    fun onSaveProfileInfo(name: String, about: String?) {
        val currentProfile = _uiState.value.profile ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (
                updateProfile(
                    name = name,
                    about = about,
                    avatarUrl = currentProfile.avatar?.large,
                    bannerUrl = currentProfile.banner,
                )
            ) {
                is Result.Success -> {
                    updateStatistics()
                    _uiState.update {
                        it.copy(
                            showEditDialog = false,
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

    fun onUpdateAvatar(avatarUrl: String) {
        val currentProfile = _uiState.value.profile ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (
                updateProfile(
                    name = currentProfile.name,
                    about = currentProfile.about,
                    avatarUrl = avatarUrl,
                    bannerUrl = currentProfile.banner,
                )
            ) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
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

    fun onUpdateBanner(bannerUrl: String) {
        val currentProfile = _uiState.value.profile ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (
                updateProfile(
                    name = currentProfile.name,
                    about = currentProfile.about,
                    avatarUrl = currentProfile.avatar?.large,
                    bannerUrl = bannerUrl,
                )
            ) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
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

    fun onRemoveAvatar() {
        val currentProfile = _uiState.value.profile ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (
                updateProfile(
                    name = currentProfile.name,
                    about = currentProfile.about,
                    avatarUrl = null,
                    bannerUrl = currentProfile.banner,
                )
            ) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
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

    fun onRemoveBanner() {
        val currentProfile = _uiState.value.profile ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (
                updateProfile(
                    name = currentProfile.name,
                    about = currentProfile.about,
                    avatarUrl = currentProfile.avatar?.large,
                    bannerUrl = null,
                )
            ) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
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

    fun onOpenFavoriteSelector(position: Int) {
        _uiState.update {
            it.copy(
                showFavoriteSelectorDialog = true,
                selectedFavoritePosition = position
            )
        }
    }

    fun onDismissFavoriteSelector() {
        _uiState.update {
            it.copy(
                showFavoriteSelectorDialog = false,
                selectedFavoritePosition = null
            )
        }
    }

    fun onSetFavorite(mediaId: Int, position: Int) {
        val mediaType = if (_uiState.value.selectedTab == 0) MediaType.ANIME else MediaType.MANGA

        viewModelScope.launch {
            when (setMediaAsFavorite(mediaId, mediaType, position)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            showFavoriteSelectorDialog = false,
                            selectedFavoritePosition = null
                        )
                    }
                }
                is Result.Failure -> {
                    _uiState.update { it.copy(error = ProfileError.SavingError) }
                }
            }
        }
    }

    fun onRemoveFavorite(mediaId: Int) {
        val mediaType = if (_uiState.value.selectedTab == 0) MediaType.ANIME else MediaType.MANGA

        viewModelScope.launch {
            when (removeFavorite(mediaId, mediaType)) {
                is Result.Success -> {
                }
                is Result.Failure -> {
                    _uiState.update { it.copy(error = ProfileError.SavingError) }
                }
            }
        }
    }

    fun onReorderFavorites(fromPosition: Int, toPosition: Int, isAnime: Boolean) {
        val mediaType = if (isAnime) MediaType.ANIME else MediaType.MANGA
        val currentFavorites = if (isAnime) _uiState.value.favoriteAnime else _uiState.value.favoriteManga

        val reorderedEntries = mutableListOf<Pair<Int, Int>>()

        val movedEntry = currentFavorites.find { it.favoritePosition == fromPosition }

        if (movedEntry != null) {
            reorderedEntries.add(movedEntry.mediaId to toPosition)

            if (fromPosition < toPosition) {
                for (pos in (fromPosition + 1)..toPosition) {
                    currentFavorites.find { it.favoritePosition == pos }?.let {
                        reorderedEntries.add(it.mediaId to (pos - 1))
                    }
                }
            } else {
                for (pos in toPosition until fromPosition) {
                    currentFavorites.find { it.favoritePosition == pos }?.let {
                        reorderedEntries.add(it.mediaId to (pos + 1))
                    }
                }
            }

            viewModelScope.launch {
                when (reorderFavorites(mediaType, reorderedEntries)) {
                    is Result.Success -> {
                    }
                    is Result.Failure -> {
                        _uiState.update { it.copy(error = ProfileError.SavingError) }
                    }
                }
            }
        }
    }
}
