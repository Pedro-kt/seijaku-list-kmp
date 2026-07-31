package com.yumedev.seijakulistkmp.features.home.presentation

import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem

data class HomeState(
    val featuredAnime: List<FeaturedMediaItem> = emptyList(),
    val featuredManga: List<FeaturedMediaItem> = emptyList(),
    val isLoadingAnime: Boolean = false,
    val isLoadingManga: Boolean = false,
    val animeError: String? = null,
    val mangaError: String? = null
)
