package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import kotlinx.coroutines.flow.Flow

class GetListEntryUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(mediaId: Int, mediaType: MediaType): Result<MediaListEntry?> {
        return repository.getEntry(mediaId, mediaType)
    }

    fun observe(mediaId: Int, mediaType: MediaType): Flow<MediaListEntry?> {
        return repository.observeEntry(mediaId, mediaType)
    }
}
