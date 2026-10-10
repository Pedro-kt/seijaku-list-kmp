package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.features.auth.domain.usecase.GetCurrentUserUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import com.yumedev.seijakulistkmp.features.detail.domain.repository.FavoriteEpisodeRepository
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.GetCurrentProfileUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class ObserveAllFavoriteEpisodesUseCase(
    private val favoriteEpisodeRepository: FavoriteEpisodeRepository,
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) {
    operator fun invoke(): Flow<List<FavoriteEpisode>> {
        return combine(getCurrentUser(), getCurrentProfile()) { user, profile ->
            when {
                user != null && !user.isAnonymous -> user.uid
                profile != null -> profile.id.toString()
                else -> null
            }
        }.flatMapLatest { userId ->
            if (userId != null) {
                favoriteEpisodeRepository.observeAllFavorites(userId)
            } else {
                flowOf(emptyList())
            }
        }
    }
}
