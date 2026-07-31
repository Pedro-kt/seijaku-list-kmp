package com.yumedev.seijakulistkmp.features.anime.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for Anime from AniList API
 */
@Serializable
data class AnimeDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: AnimeTitleDto,
    @SerialName("coverImage")
    val coverImage: AnimeCoverImageDto,
    @SerialName("format")
    val format: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("episodes")
    val episodes: Int? = null,
    @SerialName("season")
    val season: String? = null,
    @SerialName("seasonYear")
    val seasonYear: Int? = null,
    @SerialName("averageScore")
    val averageScore: Int? = null,
    @SerialName("popularity")
    val popularity: Int? = null,
    @SerialName("genres")
    val genres: List<String> = emptyList(),
    @SerialName("description")
    val description: String? = null,
    @SerialName("bannerImage")
    val bannerImage: String? = null,
    @SerialName("isAdult")
    val isAdult: Boolean = false
)

@Serializable
data class AnimeTitleDto(
    @SerialName("romaji")
    val romaji: String? = null,
    @SerialName("english")
    val english: String? = null,
    @SerialName("native")
    val native: String? = null
)

@Serializable
data class AnimeCoverImageDto(
    @SerialName("extraLarge")
    val extraLarge: String? = null,
    @SerialName("large")
    val large: String? = null,
    @SerialName("medium")
    val medium: String? = null,
    @SerialName("color")
    val color: String? = null
)

/**
 * Wrapper for paginated response
 */
@Serializable
data class PagedAnimeResponseDto(
    @SerialName("Page")
    val page: PageDataDto
)

@Serializable
data class PageDataDto(
    @SerialName("pageInfo")
    val pageInfo: PageInfoDto,
    @SerialName("media")
    val media: List<AnimeDto>
)

@Serializable
data class PageInfoDto(
    @SerialName("total")
    val total: Int? = null,
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("lastPage")
    val lastPage: Int? = null,
    @SerialName("hasNextPage")
    val hasNextPage: Boolean,
    @SerialName("perPage")
    val perPage: Int
)

/**
 * Response wrapper for single anime
 */
@Serializable
data class SingleAnimeResponseDto(
    @SerialName("Media")
    val media: AnimeDto
)
