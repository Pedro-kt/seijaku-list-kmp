package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetAiringNowAnimeQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toAiringNowAnime
import com.yumedev.seijakulistkmp.features.home.domain.model.AiringNowAnime
import com.yumedev.seijakulistkmp.features.home.domain.repository.AiringNowRepository

class AiringNowRepositoryImpl(
    private val apolloClient: ApolloClient
) : AiringNowRepository {

    override suspend fun getAiringNowAnime(page: Int, perPage: Int): Result<List<AiringNowAnime>> {
        return try {
            val response = apolloClient
                .query(GetAiringNowAnimeQuery(page = Optional.present(page), perPage = Optional.present(perPage)))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val anime = response.data?.Page?.media?.mapNotNull { it?.toAiringNowAnime() } ?: emptyList()
                Result.success(anime)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
