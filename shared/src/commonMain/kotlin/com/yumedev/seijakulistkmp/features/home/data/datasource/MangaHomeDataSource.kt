package com.yumedev.seijakulistkmp.features.home.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetMangaHomeDataQuery

interface MangaHomeDataSource {
    suspend fun getMangaHomeData(
        featuredPerPage: Int = 5,
        publishingPerPage: Int = 10,
        popularPerPage: Int = 10,
        topRatedPerPage: Int = 10,
        recentlyAddedPerPage: Int = 10,
        manhwaPerPage: Int = 10,
        manhwaCountry: String = "KR",
        isAdult: Boolean = false
    ): GetMangaHomeDataQuery.Data
}

class MangaHomeDataSourceImpl(
    private val apolloClient: ApolloClient
) : MangaHomeDataSource {
    override suspend fun getMangaHomeData(
        featuredPerPage: Int,
        publishingPerPage: Int,
        popularPerPage: Int,
        topRatedPerPage: Int,
        recentlyAddedPerPage: Int,
        manhwaPerPage: Int,
        manhwaCountry: String,
        isAdult: Boolean
    ): GetMangaHomeDataQuery.Data {
        val response = apolloClient.query(
            GetMangaHomeDataQuery(
                featuredPage = Optional.present(1),
                featuredPerPage = Optional.present(featuredPerPage),
                publishingPage = Optional.present(1),
                publishingPerPage = Optional.present(publishingPerPage),
                popularPage = Optional.present(1),
                popularPerPage = Optional.present(popularPerPage),
                topRatedPage = Optional.present(1),
                topRatedPerPage = Optional.present(topRatedPerPage),
                recentlyAddedPage = Optional.present(1),
                recentlyAddedPerPage = Optional.present(recentlyAddedPerPage),
                manhwaPage = Optional.present(1),
                manhwaPerPage = Optional.present(manhwaPerPage),
                manhwaCountry = Optional.present(manhwaCountry),
                isAdult = Optional.present(isAdult)
            )
        ).execute()

        return response.data ?: throw Exception(
            response.errors?.firstOrNull()?.message ?: "Unknown GraphQL error"
        )
    }
}
