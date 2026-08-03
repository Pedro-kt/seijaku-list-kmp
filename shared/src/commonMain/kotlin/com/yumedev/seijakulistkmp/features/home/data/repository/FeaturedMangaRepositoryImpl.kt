package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetFeaturedMangaQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toFeaturedManga
import com.yumedev.seijakulistkmp.features.home.domain.model.FeaturedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.FeaturedMangaRepository

class FeaturedMangaRepositoryImpl(
    private val apolloClient: ApolloClient
) : FeaturedMangaRepository {

    override suspend fun getFeaturedManga(page: Int, perPage: Int): Result<List<FeaturedManga>> {
        return try {
            val response = apolloClient
                .query(GetFeaturedMangaQuery(page = Optional.present(page), perPage = Optional.present(perPage)))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val manga = response.data?.Page?.media?.mapNotNull { it?.toFeaturedManga() } ?: emptyList()
                Result.success(manga)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
