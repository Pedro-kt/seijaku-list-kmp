package com.yumedev.seijakulistkmp.features.tracking.domain.model

data class CachedMediaInfo(
    val title: String,
    val coverImage: String?,
    val totalEpisodes: Int?,
    val totalChapters: Int?,
    val totalVolumes: Int?
)
