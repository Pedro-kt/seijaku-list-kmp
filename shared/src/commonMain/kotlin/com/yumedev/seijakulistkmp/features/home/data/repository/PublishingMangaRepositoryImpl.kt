package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetPublishingMangaQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toPublishingManga
import com.yumedev.seijakulistkmp.features.home.domain.model.PublishingManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.PublishingMangaRepository

class PublishingMangaRepositoryImpl(
    private val apolloClient: ApolloClient
) : PublishingMangaRepository {

    override suspend fun getPublishingManga(page: Int, perPage: Int): Result<List<PublishingManga>> {
        return try {
            val response = apolloClient
                .query(GetPublishingMangaQuery(page = Optional.present(page), perPage = Optional.present(perPage)))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val manga = response.data?.Page?.media?.mapNotNull { it?.toPublishingManga() } ?: emptyList()
                Result.success(manga)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
