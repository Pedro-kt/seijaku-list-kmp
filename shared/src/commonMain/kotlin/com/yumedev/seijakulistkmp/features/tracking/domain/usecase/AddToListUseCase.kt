package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.model.CachedMediaInfo
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository

class AddToListUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(
        mediaId: Int,
        mediaType: MediaType,
        status: MediaListStatus,
        mediaInfo: CachedMediaInfo
    ): Result<MediaListEntry> {
        return repository.addToList(mediaId, mediaType, status, mediaInfo)
    }
}
