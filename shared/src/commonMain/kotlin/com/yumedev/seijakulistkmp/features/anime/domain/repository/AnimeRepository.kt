package com.yumedev.seijakulistkmp.features.anime.domain.repository

import com.yumedev.seijakulistkmp.core.common.resource.Result
import com.yumedev.seijakulistkmp.core.domain.model.PaginatedData
import com.yumedev.seijakulistkmp.features.anime.domain.model.Anime
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Anime operations
 * Following Clean Architecture, this interface is in the domain layer
 * and will be implemented in the data layer
 */
interface AnimeRepository {
    /**
     * Get a paginated list of trending anime
     */
    fun getTrendingAnime(page: Int, perPage: Int): Flow<Result<PaginatedData<Anime>>>

    /**
     * Get a paginated list of popular anime
     */
    fun getPopularAnime(page: Int, perPage: Int): Flow<Result<PaginatedData<Anime>>>

    /**
     * Search anime by query
     */
    fun searchAnime(query: String, page: Int, perPage: Int): Flow<Result<PaginatedData<Anime>>>

    /**
     * Get anime details by ID
     */
    fun getAnimeById(id: Int): Flow<Result<Anime>>

    /**
     * Get anime by season
     */
    fun getAnimeBySeasonAndYear(
        season: String,
        year: Int,
        page: Int,
        perPage: Int
    ): Flow<Result<PaginatedData<Anime>>>
}
