package com.yumedev.seijakulistkmp.features.home.presentation

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.home.presentation.model.ErrorUiModel
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.MangaCardItem
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry

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

    val isRefreshing: Boolean = false,
    val isInitialLoading: Boolean = true,

    val selectedMediaDetail: MediaDetail? = null,
    val selectedMediaListEntry: MediaListEntry? = null,
    val isBottomSheetVisible: Boolean = false,
    val isLoadingBottomSheet: Boolean = false
) {
    val hasInitialData: Boolean
        get() = featuredManga.isNotEmpty() &&
                publishingManga.isNotEmpty() &&
                popularManga.isNotEmpty()
}
