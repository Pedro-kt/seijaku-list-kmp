package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetManhwaMangaQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toManhwaManga
import com.yumedev.seijakulistkmp.features.home.domain.model.ManhwaManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.ManhwaMangaRepository

class ManhwaMangaRepositoryImpl(
    private val apolloClient: ApolloClient
) : ManhwaMangaRepository {

    override suspend fun getManhwaManga(page: Int, perPage: Int, countryOfOrigin: String): Result<List<ManhwaManga>> {
        return try {
            val response = apolloClient
                .query(GetManhwaMangaQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage),
                    countryOfOrigin = Optional.present(countryOfOrigin.uppercase())
                ))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val manga = response.data?.Page?.media?.mapNotNull { it?.toManhwaManga() } ?: emptyList()
                Result.success(manga)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
