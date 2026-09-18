package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository
import com.yumedev.seijakulistkmp.features.tracking.data.remote.FirestoreMediaListDataSource
import com.yumedev.seijakulistkmp.features.tracking.data.remote.mapper.toDto
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry

class SaveMediaListEntryToFirestoreUseCase(
    private val authRepository: AuthRepository,
    private val firestoreDataSource: FirestoreMediaListDataSource,
) {
    suspend operator fun invoke(entry: MediaListEntry): Result<Unit> {
        val currentUser = authRepository.getCurrentUserSync()
            ?: return Result.Success(Unit)

        val uid = currentUser.uid
        val firestoreId = "${entry.mediaId}_${entry.mediaType.name}"
        val dto = entry.toDto(firestoreId)

        return when (val result = firestoreDataSource.saveEntry(uid, dto)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Failure -> result
        }
    }
}
