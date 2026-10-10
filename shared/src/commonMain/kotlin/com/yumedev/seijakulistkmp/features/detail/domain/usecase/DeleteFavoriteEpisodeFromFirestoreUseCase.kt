package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.usecase.GetCurrentUserUseCase
import com.yumedev.seijakulistkmp.features.detail.data.remote.FirestoreFavoriteEpisodeDataSource
import kotlinx.coroutines.flow.first

class DeleteFavoriteEpisodeFromFirestoreUseCase(
    private val firestoreDataSource: FirestoreFavoriteEpisodeDataSource,
    private val getCurrentUser: GetCurrentUserUseCase
) {
    suspend operator fun invoke(mediaId: Int, episodeNumber: Int): Result<Unit> {
        val currentUser = getCurrentUser().first()
        val userId = currentUser?.uid
            ?: return Result.Failure(Exception("User not authenticated"))

        return firestoreDataSource.deleteEpisode(userId, mediaId, episodeNumber)
    }
}
