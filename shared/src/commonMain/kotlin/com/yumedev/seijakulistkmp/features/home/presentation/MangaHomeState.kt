package com.yumedev.seijakulistkmp.features.home.presentation

import com.yumedev.seijakulistkmp.features.home.presentation.model.ErrorUiModel
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.MangaCardItem

data class MangaHomeState(
    val featuredManga: List<FeaturedMediaItem> = emptyList(),
    val currentFeaturedMangaIndex: Int = 0,
    val isLoadingFeaturedManga: Boolean = false,
    val featuredMangaError: ErrorUiModel? = null,

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
    val manhwaMangaError: String? = null,

    val isRefreshing: Boolean = false
)
