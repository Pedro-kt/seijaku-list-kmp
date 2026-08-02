package com.yumedev.seijakulistkmp.features.home.domain.model

data class NextSeasonAnime(
    val id: Int,
    val title: String,
    val coverImageUrl: String?,
    val averageScore: Int?,
    val format: String?,
    val episodes: Int?,
    val genres: List<String>
)
