package com.yumedev.seijakulistkmp.features.search.presentation.model

data class TrendingAnime(
    val id: Int,
    val rank: Int,
    val title: String,
    val coverImage: String,
    val rating: Double,
    val type: String,
    val episodes: Int
)
