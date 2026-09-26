package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import com.yumedev.seijakulistkmp.features.detail.domain.repository.FavoriteEpisodeRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteEpisodesByMediaUseCase(
    private val favoriteEpisodeRepository: FavoriteEpisodeRepository
) {
    operator fun invoke(userId: String, mediaId: Int): Flow<List<FavoriteEpisode>> {
        return favoriteEpisodeRepository.observeFavoritesByMedia(userId, mediaId)
    }
}
