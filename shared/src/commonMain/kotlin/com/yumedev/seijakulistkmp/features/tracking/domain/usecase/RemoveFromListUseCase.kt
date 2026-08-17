package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository

class RemoveFromListUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(
        mediaId: Int,
        mediaType: MediaType
    ): Result<Unit> {
        return repository.removeFromList(mediaId, mediaType)
    }
}
