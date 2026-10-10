package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.detail.domain.repository.FavoriteEpisodeRepository

class DeleteFavoriteEpisodeUseCase(
    private val favoriteEpisodeRepository: FavoriteEpisodeRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return favoriteEpisodeRepository.deleteFavoriteById(id)
    }
}
