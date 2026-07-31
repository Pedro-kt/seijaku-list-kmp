package com.yumedev.seijakulistkmp.features.anime.data.repository

import com.yumedev.seijakulistkmp.core.common.resource.DomainError
import com.yumedev.seijakulistkmp.core.common.resource.Result
import com.yumedev.seijakulistkmp.core.common.util.safeCall
import com.yumedev.seijakulistkmp.core.domain.model.PaginatedData
import com.yumedev.seijakulistkmp.features.anime.data.datasource.AnimeRemoteDataSource
import com.yumedev.seijakulistkmp.features.anime.data.mapper.toDomain
import com.yumedev.seijakulistkmp.features.anime.domain.model.Anime
import com.yumedev.seijakulistkmp.features.anime.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of AnimeRepository
 * Coordinates data from remote and local sources
 */
class AnimeRepositoryImpl(
    private val remoteDataSource: AnimeRemoteDataSource
) : AnimeRepository {

    override fun getTrendingAnime(page: Int, perPage: Int): Flow<Result<PaginatedData<Anime>>> = flow {
        val result = safeCall {
            remoteDataSource.getTrendingAnime(page, perPage).toDomain()
        }
        emit(result)
    }

    override fun getPopularAnime(page: Int, perPage: Int): Flow<Result<PaginatedData<Anime>>> = flow {
        val result = safeCall {
            remoteDataSource.getPopularAnime(page, perPage).toDomain()
        }
        emit(result)
    }

    override fun searchAnime(query: String, page: Int, perPage: Int): Flow<Result<PaginatedData<Anime>>> = flow {
        val result = safeCall {
            remoteDataSource.searchAnime(query, page, perPage).toDomain()
        }
        emit(result)
    }

    override fun getAnimeById(id: Int): Flow<Result<Anime>> = flow {
        val result = safeCall {
            remoteDataSource.getAnimeById(id).toDomain()
        }
        emit(result)
    }

    override fun getAnimeBySeasonAndYear(
        season: String,
        year: Int,
        page: Int,
        perPage: Int
    ): Flow<Result<PaginatedData<Anime>>> = flow {
        val result = safeCall {
            remoteDataSource.getAnimeBySeasonAndYear(season, year, page, perPage).toDomain()
        }
        emit(result)
    }
}
