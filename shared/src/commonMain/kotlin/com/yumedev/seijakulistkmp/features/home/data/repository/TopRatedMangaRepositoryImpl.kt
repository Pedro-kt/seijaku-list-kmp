package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetTopRatedMangaQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toTopRatedManga
import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.TopRatedMangaRepository

class TopRatedMangaRepositoryImpl(
    private val apolloClient: ApolloClient
) : TopRatedMangaRepository {

    override suspend fun getTopRatedManga(page: Int, perPage: Int): Result<List<TopRatedManga>> {
        return try {
            val response = apolloClient
                .query(GetTopRatedMangaQuery(page = Optional.present(page), perPage = Optional.present(perPage)))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val manga = response.data?.Page?.media?.mapNotNull { it?.toTopRatedManga() } ?: emptyList()
                Result.success(manga)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
