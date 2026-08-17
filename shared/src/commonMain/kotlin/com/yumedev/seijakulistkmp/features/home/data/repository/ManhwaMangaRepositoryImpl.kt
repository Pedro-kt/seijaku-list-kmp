package com.yumedev.seijakulistkmp.features.home.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.data.remote.graphql.GetManhwaMangaQuery
import com.yumedev.seijakulistkmp.features.home.data.mapper.toManhwaManga
import com.yumedev.seijakulistkmp.features.home.domain.model.ManhwaManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.ManhwaMangaRepository
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class ManhwaMangaRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val settingsRepository: SettingsRepository
) : ManhwaMangaRepository {

    override suspend fun getManhwaManga(page: Int, perPage: Int, countryOfOrigin: String): Result<List<ManhwaManga>> {
        return try {
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val response = apolloClient
                .query(
                    GetManhwaMangaQuery(
                        page = Optional.present(page),
                        perPage = Optional.present(perPage),
                        countryOfOrigin = Optional.present(countryOfOrigin.uppercase()),
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
                    ?.mapNotNull { it.toManhwaManga() }
                    ?: emptyList()
                Result.success(manga)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
