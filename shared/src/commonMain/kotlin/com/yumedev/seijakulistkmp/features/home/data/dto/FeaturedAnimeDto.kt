package com.yumedev.seijakulistkmp.features.home.data.dto

data class FeaturedAnimeDto(
    val id: Int,
    val title: TitleDto,
    val coverImage: CoverImageDto,
    val bannerImage: String?,
    val averageScore: Int?,
    val status: String?,
    val format: String?,
    val episodes: Int?,
    val season: String?,
    val seasonYear: Int?,
    val genres: List<String>
)

data class TitleDto(
    val romaji: String?,
    val english: String?,
    val native: String?
)

data class CoverImageDto(
    val extraLarge: String?,
    val large: String?,
    val medium: String?,
    val color: String?
)
