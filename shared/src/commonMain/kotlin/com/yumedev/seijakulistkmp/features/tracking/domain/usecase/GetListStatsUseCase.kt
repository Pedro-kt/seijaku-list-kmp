package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStats
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import kotlinx.coroutines.flow.Flow

class GetListStatsUseCase(
    private val repository: MediaListRepository
) {
    operator fun invoke(mediaType: MediaType): Flow<MediaListStats> {
        return repository.observeStats(mediaType)
    }
}
