package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMediaUseCase(
    private val repository: MediaListRepository
) {
    fun observe(mediaType: MediaType): Flow<List<MediaListEntry>> {
        return repository.observeFavorites(mediaType)
    }

    suspend operator fun invoke(mediaType: MediaType): Result<List<MediaListEntry>> {
        return repository.getFavorites(mediaType)
    }
}
