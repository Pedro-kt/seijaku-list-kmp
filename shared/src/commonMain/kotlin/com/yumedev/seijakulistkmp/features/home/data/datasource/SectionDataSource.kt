package com.yumedev.seijakulistkmp.features.home.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetSectionMediaQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaFormat
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaSort
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaStatus
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.home.domain.model.SectionType

interface SectionDataSource {
    suspend fun getSectionMedia(
        sectionType: SectionType,
        mediaType: MediaType,
        page: Int,
        perPage: Int
    ): Result<GetSectionMediaQuery.Data>
}

class SectionDataSourceImpl(
    private val apolloClient: ApolloClient
) : SectionDataSource {

    override suspend fun getSectionMedia(
        sectionType: SectionType,
        mediaType: MediaType,
        page: Int,
        perPage: Int
    ): Result<GetSectionMediaQuery.Data> {
        return try {
            val (status, sort, format, countryOfOrigin) = getSectionParameters(sectionType)

            val response = apolloClient.query(
                GetSectionMediaQuery(
                    page = Optional.present(page),
                    perPage = Optional.present(perPage),
                    type = Optional.present(
                        when (mediaType) {
                            MediaType.ANIME -> com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaType.ANIME
                            MediaType.MANGA -> com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaType.MANGA
                        }
                    ),
                    status = Optional.presentIfNotNull(status),
                    sort = Optional.presentIfNotNull(sort),
                    format = Optional.presentIfNotNull(format),
                    countryOfOrigin = Optional.presentIfNotNull(countryOfOrigin),
                    isAdult = Optional.present(false)
                )
            ).execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                response.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No data received"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getSectionParameters(
        sectionType: SectionType
    ): SectionParameters {
        return when (sectionType) {
            SectionType.AIRING_NOW -> SectionParameters(
                status = MediaStatus.RELEASING,
                sort = listOf(MediaSort.POPULARITY_DESC)
            )

            SectionType.NEXT_SEASON -> SectionParameters(
                status = MediaStatus.NOT_YET_RELEASED,
                sort = listOf(MediaSort.POPULARITY_DESC)
            )

            SectionType.TOP_RATED_ANIME -> SectionParameters(
                sort = listOf(MediaSort.SCORE_DESC)
            )

            SectionType.PUBLISHING_MANGA -> SectionParameters(
                status = MediaStatus.RELEASING,
                sort = listOf(MediaSort.POPULARITY_DESC)
            )

            SectionType.POPULAR_MANGA -> SectionParameters(
                sort = listOf(MediaSort.POPULARITY_DESC)
            )

            SectionType.TOP_RATED_MANGA -> SectionParameters(
                sort = listOf(MediaSort.SCORE_DESC)
            )

            SectionType.RECENTLY_ADDED -> SectionParameters(
                sort = listOf(MediaSort.ID_DESC)
            )

            SectionType.MANHWA -> SectionParameters(
                format = MediaFormat.MANGA,
                countryOfOrigin = "KR",
                sort = listOf(MediaSort.POPULARITY_DESC)
            )
        }
    }

    private data class SectionParameters(
        val status: MediaStatus? = null,
        val sort: List<MediaSort>? = null,
        val format: MediaFormat? = null,
        val countryOfOrigin: String? = null
    )
}
