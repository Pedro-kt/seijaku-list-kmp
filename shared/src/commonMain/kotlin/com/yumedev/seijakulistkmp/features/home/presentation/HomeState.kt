package com.yumedev.seijakulistkmp.features.home.presentation

import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.ErrorUiModel
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.MangaCardItem

data class HomeState(
    val featuredAnime: List<FeaturedMediaItem> = emptyList(),
    val featuredManga: List<FeaturedMediaItem> = emptyList(),
    val isLoadingFeatured: Boolean = false,
    val featuredError: ErrorUiModel? = null,

    val airingNowAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingAiringNow: Boolean = false,
    val airingNowError: String? = null,

    val nextSeasonAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingNextSeason: Boolean = false,
    val nextSeasonError: String? = null,

    val topRatedAnime: List<AnimeCardItem> = emptyList(),
    val isLoadingTopRated: Boolean = false,
    val topRatedError: String? = null,

    val publishingManga: List<MangaCardItem> = emptyList(),
    val isLoadingPublishingManga: Boolean = false,
    val publishingMangaError: String? = null,

    val popularManga: List<MangaCardItem> = emptyList(),
    val isLoadingPopularManga: Boolean = false,
    val popularMangaError: String? = null,

    val topRatedManga: List<MangaCardItem> = emptyList(),
    val isLoadingTopRatedManga: Boolean = false,
    val topRatedMangaError: String? = null,

    val recentlyAddedManga: List<MangaCardItem> = emptyList(),
    val isLoadingRecentlyAddedManga: Boolean = false,
    val recentlyAddedMangaError: String? = null,

    val manhwaManga: List<MangaCardItem> = emptyList(),
    val isLoadingManhwaManga: Boolean = false,
    val manhwaMangaError: String? = null
)
