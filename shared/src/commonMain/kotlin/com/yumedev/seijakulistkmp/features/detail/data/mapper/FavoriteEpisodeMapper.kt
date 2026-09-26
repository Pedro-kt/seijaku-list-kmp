package com.yumedev.seijakulistkmp.features.detail.data.mapper

import com.yumedev.seijakulistkmp.features.detail.data.local.entity.FavoriteEpisodeEntity
import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode

fun FavoriteEpisodeEntity.toDomain(): FavoriteEpisode {
    return FavoriteEpisode(
        id = id,
        userId = userId,
        mediaId = mediaId,
        episodeNumber = episodeNumber,
        episodeTitle = episodeTitle,
        thumbnailUrl = thumbnailUrl,
        markedAt = markedAt,
        syncedAt = syncedAt
    )
}

fun FavoriteEpisode.toEntity(): FavoriteEpisodeEntity {
    return FavoriteEpisodeEntity(
        id = id,
        userId = userId,
        mediaId = mediaId,
        episodeNumber = episodeNumber,
        episodeTitle = episodeTitle,
        thumbnailUrl = thumbnailUrl,
        markedAt = markedAt,
        syncedAt = syncedAt
    )
}
