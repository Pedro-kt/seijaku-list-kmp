package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.data.remote.graphql.GetAnimeHomeDataQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaSeason
import com.yumedev.seijakulistkmp.features.home.data.datasource.AnimeHomeDataSource

class GetAnimeHomeDataUseCase(
    private val animeHomeDataSource: AnimeHomeDataSource
) {
    suspend operator fun invoke(
        nextSeason: MediaSeason? = null,
        nextSeasonYear: Int? = null,
        isAdult: Boolean = false
    ): Result<GetAnimeHomeDataQuery.Data> {
        return try {
            val data = animeHomeDataSource.getAnimeHomeData(
                featuredPerPage = 5,
                airingPerPage = 10,
                nextSeasonPerPage = 10,
                nextSeason = nextSeason,
                nextSeasonYear = nextSeasonYear,
                topRatedPerPage = 10,
                isAdult = isAdult
            )
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
