package com.yumedev.seijakulistkmp.features.profile.presentation.model

import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStats

data class ProfileUiState(
    val profile: UserProfile? = null,
    val animeStats: MediaListStats? = null,
    val mangaStats: MediaListStats? = null,
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val error: ProfileError? = null,
    val isEditing: Boolean = false,
)
