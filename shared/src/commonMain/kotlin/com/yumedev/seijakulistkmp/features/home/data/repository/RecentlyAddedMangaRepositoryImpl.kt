package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetRecentlyAddedMangaQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toRecentlyAddedManga
import com.yumedev.seijakulistkmp.features.home.domain.model.RecentlyAddedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.RecentlyAddedMangaRepository

class RecentlyAddedMangaRepositoryImpl(
    private val apolloClient: ApolloClient
) : RecentlyAddedMangaRepository {

    override suspend fun getRecentlyAddedManga(page: Int, perPage: Int): Result<List<RecentlyAddedManga>> {
        return try {
            val response = apolloClient
                .query(GetRecentlyAddedMangaQuery(page = Optional.present(page), perPage = Optional.present(perPage)))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val manga = response.data?.Page?.media?.mapNotNull { it?.toRecentlyAddedManga() } ?: emptyList()
                Result.success(manga)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
