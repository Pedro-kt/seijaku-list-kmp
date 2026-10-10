package com.yumedev.seijakulistkmp.features.detail.data.remote.mapper

import com.yumedev.seijakulistkmp.features.detail.data.remote.dto.FavoriteEpisodeDto
import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode

fun FavoriteEpisode.toDto(): FavoriteEpisodeDto {
    return FavoriteEpisodeDto(
        mediaId = mediaId,
        episodeNumber = episodeNumber,
        episodeTitle = episodeTitle,
        thumbnailUrl = thumbnailUrl,
        markedAt = markedAt,
        syncedAt = syncedAt,
        rating = rating,
        comment = comment
    )
}

fun FavoriteEpisodeDto.toDomain(id: Long = 0, userId: String): FavoriteEpisode {
    return FavoriteEpisode(
        id = id,
        userId = userId,
        mediaId = mediaId,
        episodeNumber = episodeNumber,
        episodeTitle = episodeTitle,
        thumbnailUrl = thumbnailUrl,
        markedAt = markedAt,
        syncedAt = syncedAt,
        rating = rating,
        comment = comment
    )
}
