package com.yumedev.seijakulistkmp.features.home.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetFeaturedAnimeQuery

/**
 * Data source específico para Featured Carousel
 * Solo maneja la query de trending anime para el carousel principal
 */
interface FeaturedDataSource {
    suspend fun getFeaturedAnime(page: Int, perPage: Int): GetFeaturedAnimeQuery.Data
}

/**
 * Implementación del data source usando Apollo GraphQL
 */
class FeaturedDataSourceImpl(
    private val apolloClient: ApolloClient
) : FeaturedDataSource {

    override suspend fun getFeaturedAnime(page: Int, perPage: Int): GetFeaturedAnimeQuery.Data {
        val response = apolloClient
            .query(
                GetFeaturedAnimeQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage)
                )
            )
            .execute()

        return response.data ?: throw Exception(
            response.errors?.firstOrNull()?.message ?: "Unknown GraphQL error"
        )
    }
}
