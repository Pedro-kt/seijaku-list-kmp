package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetNextSeasonAnimeQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaSeason
import com.yumedev.seijakulistkmp.features.home.data.mapper.toNextSeasonAnime
import com.yumedev.seijakulistkmp.features.home.domain.model.NextSeasonAnime
import com.yumedev.seijakulistkmp.features.home.domain.repository.NextSeasonRepository
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.*

class NextSeasonRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val settingsRepository: SettingsRepository
) : NextSeasonRepository {

    override suspend fun getNextSeasonAnime(page: Int, perPage: Int): Result<List<NextSeasonAnime>> {
        return try {
            val (season, year) = getNextSeason()
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val response = apolloClient
                .query(
                    GetNextSeasonAnimeQuery(
                        page = Optional.present(page),
                        perPage = Optional.present(perPage),
                        season = Optional.present(season),
                        seasonYear = Optional.present(year),
                        isAdult = if (isAdultFilter != null) Optional.present(isAdultFilter) else Optional.absent()
                    )
                )
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val anime = response.data?.Page?.media
                    ?.filterNotNull()
                    ?.filter { !(it.isAdult ?: false) }
                    ?.mapNotNull { it.toNextSeasonAnime() }
                    ?: emptyList()
                Result.success(anime)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getNextSeason(): Pair<MediaSeason, Int> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentMonth = now.monthNumber
        val currentYear = now.year

        return when (currentMonth) {
            in 1..3 -> MediaSeason.SPRING to currentYear
            in 4..6 -> MediaSeason.SUMMER to currentYear
            in 7..9 -> MediaSeason.FALL to currentYear
            else -> MediaSeason.WINTER to (currentYear + 1)
        }
    }
}
