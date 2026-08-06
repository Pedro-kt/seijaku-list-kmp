package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.model.FeaturedManga
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem

class FeaturedMangaUiMapper(
    private val formatter: MediaStringFormatter
) {
    suspend fun map(manga: FeaturedManga): FeaturedMediaItem {
        return FeaturedMediaItem(
            id = manga.id,
            title = manga.title,
            coverImageUrl = manga.bannerImageUrl ?: manga.coverImageUrl,
            rating = formatter.formatRating(manga.averageScore),
            status = formatter.formatStatus(manga.status, isManga = true),
            metadata = formatter.buildMangaMetadata(
                startYear = null,
                format = manga.format,
                chapters = manga.chapters,
                volumes = manga.volumes
            )
        )
    }
}

// Extension function for backward compatibility
suspend fun FeaturedManga.toFeaturedMediaItem(formatter: MediaStringFormatter): FeaturedMediaItem {
    return FeaturedMangaUiMapper(formatter).map(this)
}
