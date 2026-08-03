package com.yumedev.seijakulistkmp.features.search.presentation.model

data class SearchResultItem(
    val id: Int,
    val title: String,
    val alternativeTitle: String? = null,
    val coverImage: String,
    val rating: Double? = null,
    val year: Int? = null,
    val type: String,
    val status: String? = null,
    val episodes: Int? = null,
    val chapters: Int? = null, // For manga
    val volumes: Int? = null, // For manga
    val genres: List<String> = emptyList(),
    val mediaType: MediaType // ANIME or MANGA
)

enum class MediaType {
    ANIME,
    MANGA
}
