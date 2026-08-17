package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetTopRatedMangaQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toTopRatedManga
import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.TopRatedMangaRepository
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class TopRatedMangaRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val settingsRepository: SettingsRepository
) : TopRatedMangaRepository {

    override suspend fun getTopRatedManga(page: Int, perPage: Int): Result<List<TopRatedManga>> {
        return try {
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val response = apolloClient
                .query(
                    GetTopRatedMangaQuery(
                        page = Optional.present(page),
                        perPage = Optional.present(perPage),
                        isAdult = if (isAdultFilter != null) Optional.present(isAdultFilter) else Optional.absent()
                    )
                )
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val manga = response.data?.Page?.media
                    ?.filterNotNull()
                    ?.filter { !(it.isAdult ?: false) }
                    ?.mapNotNull { it.toTopRatedManga() }
                    ?: emptyList()
                Result.success(manga)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
