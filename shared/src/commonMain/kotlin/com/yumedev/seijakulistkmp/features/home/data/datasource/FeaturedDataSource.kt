package com.yumedev.seijakulistkmp.features.home.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.core.error.GraphQLErrorException
import com.yumedev.seijakulistkmp.data.remote.graphql.GetFeaturedAnimeQuery

interface FeaturedDataSource {
    suspend fun getFeaturedAnime(page: Int, perPage: Int, isAdult: Boolean?): GetFeaturedAnimeQuery.Data
}

class FeaturedDataSourceImpl(
    private val apolloClient: ApolloClient
) : FeaturedDataSource {

    override suspend fun getFeaturedAnime(page: Int, perPage: Int, isAdult: Boolean?): GetFeaturedAnimeQuery.Data {
        val response = apolloClient
            .query(
                GetFeaturedAnimeQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage),
                    isAdult = if (isAdult != null) Optional.present(isAdult) else Optional.absent()
                )
            )
            .execute()

        // Check for exception first
        response.exception?.let { exc ->
            throw exc
        }

        response.errors?.firstOrNull()?.let { error ->
            val statusCode = try {
                error.extensions?.get("status") as? Int
            } catch (e: Exception) {
                null
            }

            throw GraphQLErrorException(
                message = error.message ?: "Unknown GraphQL error",
                statusCode = statusCode ?: 0
            )
        }

        return response.data ?: throw Exception("No data returned from GraphQL query")
    }
}
