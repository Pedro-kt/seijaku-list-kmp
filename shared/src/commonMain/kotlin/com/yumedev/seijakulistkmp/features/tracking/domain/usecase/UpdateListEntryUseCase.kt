package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository

class UpdateListEntryUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(
        mediaId: Int,
        mediaType: MediaType,
        status: MediaListStatus? = null,
        progress: Int? = null,
        progressVolumes: Int? = null,
        score: Float? = null,
        startDate: String? = null,
        finishDate: String? = null,
        notes: String? = null,
        repeatCount: Int? = null,
        priority: MediaListPriority? = null
    ): Result<MediaListEntry> {
        return repository.updateEntry(
            mediaId, mediaType, status, progress, progressVolumes,
            score, startDate, finishDate, notes, repeatCount, priority
        )
    }
}
