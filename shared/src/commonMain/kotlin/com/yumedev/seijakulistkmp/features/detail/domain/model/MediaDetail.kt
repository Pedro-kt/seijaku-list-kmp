package com.yumedev.seijakulistkmp.features.detail.domain.model

data class MediaDetail(
    val id: Int,
    val title: String,
    val titleNative: String?,
    val coverImageUrl: String?,
    val bannerImageUrl: String?,
    val type: MediaType,
    val format: String?,
    val demographic: String?,
    val year: Int?,
    val status: String?,
    val episodes: Int?,
    val chapters: Int?,
    val volumes: Int?,
    val startDate: String?,
    val endDate: String?,
    val serialization: String?,
    val averageScore: Double?,
    val rankingPosition: Int?,
    val popularityPosition: Int?,
    val totalVotes: Int?,
    val description: String?,
    val author: String?,
    val artist: String?,
    val studio: String?,
    val license: String?,
    val genres: List<String>,
    val mainCharacters: List<Character>,
    val episodes_list: List<Episode>?,
    val chapters_list: List<Chapter>?,
    val images: List<String>,
    val isFavorite: Boolean = false,
    val isInList: Boolean = false
)

enum class MediaType {
    ANIME,
    MANGA
}

data class Character(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val role: String
)

data class Episode(
    val number: Int,
    val title: String,
    val airDate: String?,
    val duration: Int?,
    val rating: Double?
)

data class Chapter(
    val number: Int,
    val title: String,
    val volume: Int?,
    val releaseDate: String?,
    val rating: Double?
)
