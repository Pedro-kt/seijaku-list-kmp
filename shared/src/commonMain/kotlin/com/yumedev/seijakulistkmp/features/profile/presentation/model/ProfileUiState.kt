package com.yumedev.seijakulistkmp.features.profile.presentation.model

import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStats

data class ProfileUiState(
    val profile: UserProfile? = null,
    val animeStats: MediaListStats? = null,
    val mangaStats: MediaListStats? = null,
    val favoriteAnime: List<MediaListEntry> = emptyList(),
    val favoriteManga: List<MediaListEntry> = emptyList(),
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val error: ProfileError? = null,
    val showEditDialog: Boolean = false,
    val showFavoriteSelectorDialog: Boolean = false,
    val selectedFavoritePosition: Int? = null,
    val isAuthenticated: Boolean = false,
)
