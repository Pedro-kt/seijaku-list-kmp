package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository

class ReorderFavoritesUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(
        mediaType: MediaType,
        reorderedEntries: List<Pair<Int, Int>>
    ): Result<Unit> {
        reorderedEntries.forEach { (_, position) ->
            require(position in 1..5) { "Favorite position must be between 1 and 5" }
        }
        return repository.reorderFavorites(mediaType, reorderedEntries)
    }
}
