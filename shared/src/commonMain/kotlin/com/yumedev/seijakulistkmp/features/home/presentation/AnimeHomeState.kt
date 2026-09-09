package com.yumedev.seijakulistkmp.features.home.presentation

import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.ErrorUiModel
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem

data class AnimeHomeState(
    val featuredAnime: List<FeaturedMediaItem> = emptyList(),
    val currentFeaturedAnimeIndex: Int = 0,
    val isLoadingFeaturedAnime: Boolean = false,
    val featuredAnimeError: ErrorUiModel? = null,

    val airingNowAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingAiringNow: Boolean = false,
    val airingNowError: String? = null,

    val nextSeasonAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingNextSeason: Boolean = false,
    val nextSeasonError: String? = null,

    val topRatedAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingTopRated: Boolean = false,
    val topRatedError: String? = null,

    val isRefreshing: Boolean = false
)
