package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import com.yumedev.seijakulistkmp.features.detail.domain.repository.FavoriteEpisodeRepository
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.GetCurrentProfileUseCase
import kotlinx.coroutines.flow.first

class GetAllFavoriteEpisodesUseCase(
    private val favoriteEpisodeRepository: FavoriteEpisodeRepository,
    private val getCurrentProfile: GetCurrentProfileUseCase
) {
    suspend operator fun invoke(): Result<List<FavoriteEpisode>> {
        val currentProfile = getCurrentProfile().first()
        val userId = currentProfile?.id?.toString() ?: return Result.Success(emptyList())

        return favoriteEpisodeRepository.getAllFavorites(userId)
    }
}
