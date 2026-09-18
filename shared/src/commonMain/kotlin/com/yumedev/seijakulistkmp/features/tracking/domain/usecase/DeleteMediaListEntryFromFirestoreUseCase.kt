package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository
import com.yumedev.seijakulistkmp.features.tracking.data.remote.FirestoreMediaListDataSource

class DeleteMediaListEntryFromFirestoreUseCase(
    private val authRepository: AuthRepository,
    private val firestoreDataSource: FirestoreMediaListDataSource,
) {
    suspend operator fun invoke(mediaId: Int, mediaType: MediaType): Result<Unit> {
        val currentUser = authRepository.getCurrentUserSync()
            ?: return Result.Success(Unit)

        val uid = currentUser.uid
        val firestoreId = "${mediaId}_${mediaType.name}"

        return firestoreDataSource.deleteEntry(uid, firestoreId)
    }
}
