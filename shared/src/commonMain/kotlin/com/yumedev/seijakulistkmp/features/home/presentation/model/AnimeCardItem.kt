package com.yumedev.seijakulistkmp.features.home.presentation.model

data class AnimeCardItem(
    val id: Int,
    val title: String,
    val coverImageUrl: String?,
    val rating: String?,
    val format: String?,
    val episodes: String?,
    val genres: List<String>
)
