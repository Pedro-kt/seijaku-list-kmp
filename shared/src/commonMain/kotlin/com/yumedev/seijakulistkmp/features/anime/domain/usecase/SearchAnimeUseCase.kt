package com.yumedev.seijakulistkmp.features.anime.domain.usecase

import com.yumedev.seijakulistkmp.core.common.resource.Result
import com.yumedev.seijakulistkmp.core.domain.model.PaginatedData
import com.yumedev.seijakulistkmp.features.anime.domain.model.Anime
import com.yumedev.seijakulistkmp.features.anime.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case to search anime
 */
class SearchAnimeUseCase(
    private val repository: AnimeRepository
) {
    operator fun invoke(query: String, page: Int = 1, perPage: Int = 20): Flow<Result<PaginatedData<Anime>>> {
        if (query.isBlank()) {
            return flow { emit(Result.failure("Search query cannot be empty")) }
        }
        return repository.searchAnime(query.trim(), page, perPage)
    }
}
