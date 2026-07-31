package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetTopRatedAnimeQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toTopRatedAnime
import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedAnime
import com.yumedev.seijakulistkmp.features.home.domain.repository.TopRatedRepository

class TopRatedRepositoryImpl(
    private val apolloClient: ApolloClient
) : TopRatedRepository {

    override suspend fun getTopRatedAnime(page: Int, perPage: Int): Result<List<TopRatedAnime>> {
        return try {
            val response = apolloClient
                .query(GetTopRatedAnimeQuery(page = Optional.present(page), perPage = Optional.present(perPage)))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val anime = response.data?.Page?.media?.mapNotNull { it?.toTopRatedAnime() } ?: emptyList()
                Result.success(anime)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
