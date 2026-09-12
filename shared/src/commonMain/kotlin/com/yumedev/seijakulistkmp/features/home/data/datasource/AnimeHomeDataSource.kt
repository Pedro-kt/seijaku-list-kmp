package com.yumedev.seijakulistkmp.features.home.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetAnimeHomeDataQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaSeason

interface AnimeHomeDataSource {
    suspend fun getAnimeHomeData(
        featuredPerPage: Int = 5,
        airingPerPage: Int = 10,
        nextSeasonPerPage: Int = 10,
        nextSeason: MediaSeason?,
        nextSeasonYear: Int?,
        topRatedPerPage: Int = 10,
        isAdult: Boolean = false
    ): GetAnimeHomeDataQuery.Data
}

class AnimeHomeDataSourceImpl(
    private val apolloClient: ApolloClient
) : AnimeHomeDataSource {
    override suspend fun getAnimeHomeData(
        featuredPerPage: Int,
        airingPerPage: Int,
        nextSeasonPerPage: Int,
        nextSeason: MediaSeason?,
        nextSeasonYear: Int?,
        topRatedPerPage: Int,
        isAdult: Boolean
    ): GetAnimeHomeDataQuery.Data {
        val response = apolloClient.query(
            GetAnimeHomeDataQuery(
                featuredPage = Optional.present(1),
                featuredPerPage = Optional.present(featuredPerPage),
                airingPage = Optional.present(1),
                airingPerPage = Optional.present(airingPerPage),
                nextSeasonPage = Optional.present(1),
                nextSeasonPerPage = Optional.present(nextSeasonPerPage),
                nextSeason = Optional.presentIfNotNull(nextSeason),
                nextSeasonYear = Optional.presentIfNotNull(nextSeasonYear),
                topRatedPage = Optional.present(1),
                topRatedPerPage = Optional.present(topRatedPerPage),
                isAdult = Optional.present(isAdult)
            )
        ).execute()

        return response.data ?: throw Exception(
            response.errors?.firstOrNull()?.message ?: "Unknown GraphQL error"
        )
    }
}
