package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CheckInListUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(mediaId: Int, mediaType: MediaType): Boolean {
        return repository.getEntry(mediaId, mediaType).getOrNull() != null
    }

    fun observe(mediaId: Int, mediaType: MediaType): Flow<Boolean> {
        return repository.observeEntry(mediaId, mediaType)
            .map { it != null }
    }
}
