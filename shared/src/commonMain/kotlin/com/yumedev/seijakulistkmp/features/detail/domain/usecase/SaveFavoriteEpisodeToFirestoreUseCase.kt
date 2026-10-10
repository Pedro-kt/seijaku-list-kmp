package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.usecase.GetCurrentUserUseCase
import com.yumedev.seijakulistkmp.features.detail.data.remote.FirestoreFavoriteEpisodeDataSource
import com.yumedev.seijakulistkmp.features.detail.data.remote.mapper.toDto
import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import kotlinx.coroutines.flow.first

class SaveFavoriteEpisodeToFirestoreUseCase(
    private val firestoreDataSource: FirestoreFavoriteEpisodeDataSource,
    private val getCurrentUser: GetCurrentUserUseCase
) {
    suspend operator fun invoke(episode: FavoriteEpisode): Result<Unit> {
        val currentUser = getCurrentUser().first()
        val userId = currentUser?.uid
            ?: return Result.Failure(Exception("User not authenticated"))

        return firestoreDataSource.saveEpisode(userId, episode.toDto())
    }
}
