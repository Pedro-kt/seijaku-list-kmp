package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.data.dto.FeaturedAnimeDto
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem

class FeaturedUiMapper(
    private val formatter: MediaStringFormatter
) {
    suspend fun map(dto: FeaturedAnimeDto): FeaturedMediaItem {
        val displayTitle = dto.title.english ?: dto.title.romaji ?: dto.title.native ?: formatter.getUnknownString()

        return FeaturedMediaItem(
            id = dto.id,
            title = displayTitle,
            coverImageUrl = dto.bannerImage ?: (dto.coverImage.extraLarge ?: dto.coverImage.large ?: dto.coverImage.medium),
            rating = formatter.formatRating(dto.averageScore),
            status = formatter.formatStatus(dto.status),
            metadata = formatter.buildAnimeMetadata(dto.seasonYear, dto.format, dto.episodes)
        )
    }
}

suspend fun FeaturedAnimeDto.toFeaturedMediaItem(formatter: MediaStringFormatter): FeaturedMediaItem {
    return FeaturedUiMapper(formatter).map(this)
}
