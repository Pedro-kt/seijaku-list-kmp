package com.yumedev.seijakulistkmp.features.home.domain.model

data class FeaturedManga(
    val id: Int,
    val title: String,
    val coverImageUrl: String?,
    val bannerImageUrl: String?,
    val averageScore: Int?,
    val status: String?,
    val format: String?,
    val chapters: Int?,
    val volumes: Int?,
    val genres: List<String>
)
