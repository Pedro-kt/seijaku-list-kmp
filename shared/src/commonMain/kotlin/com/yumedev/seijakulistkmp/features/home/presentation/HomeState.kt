package com.yumedev.seijakulistkmp.features.home.presentation

import com.yumedev.seijakulistkmp.core.error.ErrorType
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem

data class HomeState(
    val featuredAnime: List<FeaturedMediaItem> = emptyList(),
    val featuredManga: List<FeaturedMediaItem> = emptyList(),
    val isLoadingFeatured: Boolean = false,
    val featuredError: ErrorType? = null,

    val airingNowAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingAiringNow: Boolean = false,
    val airingNowError: String? = null,

    val nextSeasonAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingNextSeason: Boolean = false,
    val nextSeasonError: String? = null,

    val topRatedAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingTopRated: Boolean = false,
    val topRatedError: String? = null
)
