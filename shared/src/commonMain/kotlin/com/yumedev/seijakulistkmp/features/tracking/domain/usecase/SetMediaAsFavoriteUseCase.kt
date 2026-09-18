package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository

class SetMediaAsFavoriteUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(
        mediaId: Int,
        mediaType: MediaType,
        position: Int
    ): Result<MediaListEntry> {
        require(position in 1..5) { "Favorite position must be between 1 and 5" }
        return repository.setFavoritePosition(mediaId, mediaType, position)
    }
}
