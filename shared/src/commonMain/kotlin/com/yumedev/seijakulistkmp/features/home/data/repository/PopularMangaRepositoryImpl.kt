package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetPopularMangaQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toPopularManga
import com.yumedev.seijakulistkmp.features.home.domain.model.PopularManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.PopularMangaRepository

class PopularMangaRepositoryImpl(
    private val apolloClient: ApolloClient
) : PopularMangaRepository {

    override suspend fun getPopularManga(page: Int, perPage: Int): Result<List<PopularManga>> {
        return try {
            val response = apolloClient
                .query(GetPopularMangaQuery(page = Optional.present(page), perPage = Optional.present(perPage)))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val manga = response.data?.Page?.media?.mapNotNull { it?.toPopularManga() } ?: emptyList()
                Result.success(manga)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
