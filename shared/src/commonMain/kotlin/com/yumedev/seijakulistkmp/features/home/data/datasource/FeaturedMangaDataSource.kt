package com.yumedev.seijakulistkmp.features.home.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.core.error.GraphQLErrorException
import com.yumedev.seijakulistkmp.data.remote.graphql.GetFeaturedMangaQuery

interface FeaturedMangaDataSource {
    suspend fun getFeaturedManga(page: Int, perPage: Int, isAdult: Boolean?): GetFeaturedMangaQuery.Data
}

class FeaturedMangaDataSourceImpl(
    private val apolloClient: ApolloClient
) : FeaturedMangaDataSource {

    override suspend fun getFeaturedManga(page: Int, perPage: Int, isAdult: Boolean?): GetFeaturedMangaQuery.Data {
        val response = apolloClient
            .query(
                GetFeaturedMangaQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage),
                    isAdult = if (isAdult != null) Optional.present(isAdult) else Optional.absent()
                )
            )
            .execute()

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
