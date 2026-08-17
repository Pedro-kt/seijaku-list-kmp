package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetTopRatedAnimeQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toTopRatedAnime
import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedAnime
import com.yumedev.seijakulistkmp.features.home.domain.repository.TopRatedRepository
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class TopRatedRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val settingsRepository: SettingsRepository
) : TopRatedRepository {

    override suspend fun getTopRatedAnime(page: Int, perPage: Int): Result<List<TopRatedAnime>> {
        return try {
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val response = apolloClient
                .query(
                    GetTopRatedAnimeQuery(
                        page = Optional.present(page),
                        perPage = Optional.present(perPage),
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
                    ?.mapNotNull { it.toTopRatedAnime() }
                    ?: emptyList()
                Result.success(anime)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
