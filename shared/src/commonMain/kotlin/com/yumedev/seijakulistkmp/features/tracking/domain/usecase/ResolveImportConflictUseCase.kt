package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ConflictResolution
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportConflict
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository

class ResolveImportConflictUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(
        conflict: ImportConflict,
        resolution: ConflictResolution
    ): Result<MediaListEntry> {
        return repository.resolveConflict(conflict, resolution)
    }
}
