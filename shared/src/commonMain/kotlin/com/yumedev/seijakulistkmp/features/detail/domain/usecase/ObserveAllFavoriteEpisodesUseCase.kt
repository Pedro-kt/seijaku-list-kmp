package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import com.yumedev.seijakulistkmp.features.detail.domain.repository.FavoriteEpisodeRepository
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.GetCurrentProfileUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class ObserveAllFavoriteEpisodesUseCase(
    private val favoriteEpisodeRepository: FavoriteEpisodeRepository,
    private val getCurrentProfile: GetCurrentProfileUseCase
) {
    operator fun invoke(): Flow<List<FavoriteEpisode>> {
        return getCurrentProfile().flatMapLatest { profile ->
            val userId = profile?.id?.toString()
            if (userId != null) {
                favoriteEpisodeRepository.observeAllFavorites(userId)
            } else {
                flowOf(emptyList())
            }
        }
    }
}
