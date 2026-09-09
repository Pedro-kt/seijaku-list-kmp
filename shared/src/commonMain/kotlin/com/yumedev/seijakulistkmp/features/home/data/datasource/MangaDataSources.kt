package com.yumedev.seijakulistkmp.features.home.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.core.error.GraphQLErrorException
import com.yumedev.seijakulistkmp.data.remote.graphql.*

interface PublishingMangaDataSource {
    suspend fun getPublishingManga(page: Int, perPage: Int, isAdult: Boolean?): GetPublishingMangaQuery.Data
}

class PublishingMangaDataSourceImpl(
    private val apolloClient: ApolloClient
) : PublishingMangaDataSource {
    override suspend fun getPublishingManga(page: Int, perPage: Int, isAdult: Boolean?): GetPublishingMangaQuery.Data {
        return executeQuery(
            apolloClient.query(
                GetPublishingMangaQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage),
                    isAdult = if (isAdult != null) Optional.present(isAdult) else Optional.absent()
                )
            ).execute()
        )
    }
}

interface PopularMangaDataSource {
    suspend fun getPopularManga(page: Int, perPage: Int, isAdult: Boolean?): GetPopularMangaQuery.Data
}

class PopularMangaDataSourceImpl(
    private val apolloClient: ApolloClient
) : PopularMangaDataSource {
    override suspend fun getPopularManga(page: Int, perPage: Int, isAdult: Boolean?): GetPopularMangaQuery.Data {
        return executeQuery(
            apolloClient.query(
                GetPopularMangaQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage),
                    isAdult = if (isAdult != null) Optional.present(isAdult) else Optional.absent()
                )
            ).execute()
        )
    }
}

interface TopRatedMangaDataSource {
    suspend fun getTopRatedManga(page: Int, perPage: Int, isAdult: Boolean?): GetTopRatedMangaQuery.Data
}

class TopRatedMangaDataSourceImpl(
    private val apolloClient: ApolloClient
) : TopRatedMangaDataSource {
    override suspend fun getTopRatedManga(page: Int, perPage: Int, isAdult: Boolean?): GetTopRatedMangaQuery.Data {
        return executeQuery(
            apolloClient.query(
                GetTopRatedMangaQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage),
                    isAdult = if (isAdult != null) Optional.present(isAdult) else Optional.absent()
                )
            ).execute()
        )
    }
}

interface RecentlyAddedMangaDataSource {
    suspend fun getRecentlyAddedManga(page: Int, perPage: Int, isAdult: Boolean?): GetRecentlyAddedMangaQuery.Data
}

class RecentlyAddedMangaDataSourceImpl(
    private val apolloClient: ApolloClient
) : RecentlyAddedMangaDataSource {
    override suspend fun getRecentlyAddedManga(page: Int, perPage: Int, isAdult: Boolean?): GetRecentlyAddedMangaQuery.Data {
        return executeQuery(
            apolloClient.query(
                GetRecentlyAddedMangaQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage),
                    isAdult = if (isAdult != null) Optional.present(isAdult) else Optional.absent()
                )
            ).execute()
        )
    }
}

interface ManhwaMangaDataSource {
    suspend fun getManhwaManga(page: Int, perPage: Int, countryOfOrigin: String, isAdult: Boolean?): GetManhwaMangaQuery.Data
}

class ManhwaMangaDataSourceImpl(
    private val apolloClient: ApolloClient
) : ManhwaMangaDataSource {
    override suspend fun getManhwaManga(page: Int, perPage: Int, countryOfOrigin: String, isAdult: Boolean?): GetManhwaMangaQuery.Data {
        return executeQuery(
            apolloClient.query(
                GetManhwaMangaQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage),
                    countryOfOrigin = Optional.present(countryOfOrigin.uppercase()),
                    isAdult = if (isAdult != null) Optional.present(isAdult) else Optional.absent()
                )
            ).execute()
        )
    }
}

private fun <D : com.apollographql.apollo.api.Query.Data> executeQuery(
    response: com.apollographql.apollo.api.ApolloResponse<D>
): D {
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
