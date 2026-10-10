package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.detail.domain.repository.FavoriteEpisodeRepository

class UpdateFavoriteEpisodeUseCase(
    private val favoriteEpisodeRepository: FavoriteEpisodeRepository
) {
    suspend operator fun invoke(
        id: Long,
        rating: Int?,
        comment: String?
    ): Result<Unit> {
        return favoriteEpisodeRepository.updateRatingAndComment(
            id = id,
            rating = rating,
            comment = comment
        )
    }
}
