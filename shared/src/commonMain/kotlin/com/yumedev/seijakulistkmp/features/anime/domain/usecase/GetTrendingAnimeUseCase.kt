package com.yumedev.seijakulistkmp.features.anime.domain.usecase

import com.yumedev.seijakulistkmp.core.common.resource.Result
import com.yumedev.seijakulistkmp.core.domain.model.PaginatedData
import com.yumedev.seijakulistkmp.features.anime.domain.model.Anime
import com.yumedev.seijakulistkmp.features.anime.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get trending anime
 * Encapsulates the business logic for getting trending anime
 */
class GetTrendingAnimeUseCase(
    private val repository: AnimeRepository
) {
    operator fun invoke(page: Int = 1, perPage: Int = 20): Flow<Result<PaginatedData<Anime>>> {
        return repository.getTrendingAnime(page, perPage)
    }
}
