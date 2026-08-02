package com.yumedev.seijakulistkmp.features.home.data.dto

/**
 * DTO for Airing Now anime section
 */
data class AiringNowAnimeDto(
    val id: Int,
    val title: TitleDto,
    val coverImage: CoverImageDto,
    val averageScore: Int?,
    val format: String?,
    val episodes: Int?,
    val genres: List<String>
)
