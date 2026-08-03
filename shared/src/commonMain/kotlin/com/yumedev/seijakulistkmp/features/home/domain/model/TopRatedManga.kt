package com.yumedev.seijakulistkmp.features.home.domain.model

data class TopRatedManga(
    val id: Int,
    val title: String,
    val coverImageUrl: String?,
    val averageScore: Int?,
    val format: String?,
    val status: String?,
    val chapters: Int?,
    val volumes: Int?,
    val genres: List<String>
)
