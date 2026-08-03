package com.yumedev.seijakulistkmp.features.home.domain.model

data class ManhwaManga(
    val id: Int,
    val title: String,
    val coverImageUrl: String?,
    val averageScore: Int?,
    val format: String?,
    val chapters: Int?,
    val volumes: Int?,
    val genres: List<String>,
    val countryOfOrigin: String?
)
