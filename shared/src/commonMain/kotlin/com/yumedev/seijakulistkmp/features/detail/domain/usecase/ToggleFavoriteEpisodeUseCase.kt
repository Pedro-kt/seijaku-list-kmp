package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.features.detail.domain.repository.FavoriteEpisodeRepository

class ToggleFavoriteEpisodeUseCase(
    private val favoriteEpisodeRepository: FavoriteEpisodeRepository
) {
    suspend operator fun invoke(
        userId: String,
        mediaId: Int,
        episodeNumber: Int,
        episodeTitle: String,
        thumbnailUrl: String?
    ): Result<Unit> {
        return favoriteEpisodeRepository.toggleFavorite(
            userId = userId,
            mediaId = mediaId,
            episodeNumber = episodeNumber,
            episodeTitle = episodeTitle,
            thumbnailUrl = thumbnailUrl
        )
    }
}
