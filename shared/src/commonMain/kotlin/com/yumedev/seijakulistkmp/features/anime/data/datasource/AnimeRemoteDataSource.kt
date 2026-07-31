package com.yumedev.seijakulistkmp.features.anime.data.datasource

import com.yumedev.seijakulistkmp.features.anime.data.dto.PagedAnimeResponseDto
import com.yumedev.seijakulistkmp.features.anime.data.dto.SingleAnimeResponseDto

/**
 * Interface for remote data source
 * Will be implemented using Apollo GraphQL client
 */
interface AnimeRemoteDataSource {
    suspend fun getTrendingAnime(page: Int, perPage: Int): PagedAnimeResponseDto
    suspend fun getPopularAnime(page: Int, perPage: Int): PagedAnimeResponseDto
    suspend fun searchAnime(query: String, page: Int, perPage: Int): PagedAnimeResponseDto
    suspend fun getAnimeById(id: Int): SingleAnimeResponseDto
    suspend fun getAnimeBySeasonAndYear(
        season: String,
        year: Int,
        page: Int,
        perPage: Int
    ): PagedAnimeResponseDto
}
