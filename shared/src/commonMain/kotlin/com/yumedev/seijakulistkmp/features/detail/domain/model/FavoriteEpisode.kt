package com.yumedev.seijakulistkmp.features.detail.domain.model

data class FavoriteEpisode(
    val id: Long = 0,
    val userId: String,
    val mediaId: Int,
    val episodeNumber: Int,
    val episodeTitle: String,
    val thumbnailUrl: String?,
    val markedAt: Long,
    val syncedAt: Long? = null
)
