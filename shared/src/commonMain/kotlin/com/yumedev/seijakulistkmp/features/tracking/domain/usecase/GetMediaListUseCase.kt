package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import kotlinx.coroutines.flow.Flow

class GetMediaListUseCase(
    private val repository: MediaListRepository
) {
    operator fun invoke(
        mediaType: MediaType,
        status: MediaListStatus? = null
    ): Flow<List<MediaListEntry>> {
        return repository.observeList(mediaType, status)
    }
}
