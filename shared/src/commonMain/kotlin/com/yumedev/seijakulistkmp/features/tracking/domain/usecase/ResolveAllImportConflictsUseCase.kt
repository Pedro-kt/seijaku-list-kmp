package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ConflictResolution
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportConflict
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository

class ResolveAllImportConflictsUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(
        conflicts: List<ImportConflict>,
        resolution: ConflictResolution
    ): Result<List<MediaListEntry>> {
        return repository.resolveAllConflicts(conflicts, resolution)
    }
}
