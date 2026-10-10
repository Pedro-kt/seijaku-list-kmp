package com.yumedev.seijakulistkmp.features.detail.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteEpisodeDto(
    val mediaId: Int = 0,
    val episodeNumber: Int = 0,
    val episodeTitle: String = "",
    val thumbnailUrl: String? = null,
    val markedAt: Long = 0,
    val syncedAt: Long? = null,
    val rating: Int? = null,
    val comment: String? = null
)
