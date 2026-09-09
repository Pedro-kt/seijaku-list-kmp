package com.yumedev.seijakulistkmp.features.home.data.repository

import com.yumedev.seijakulistkmp.features.home.data.datasource.PopularMangaDataSource
import com.yumedev.seijakulistkmp.features.home.data.mapper.toPopularManga
import com.yumedev.seijakulistkmp.features.home.domain.model.PopularManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.PopularMangaRepository
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class PopularMangaRepositoryImpl(
    private val dataSource: PopularMangaDataSource,
    private val settingsRepository: SettingsRepository
) : PopularMangaRepository {

    override suspend fun getPopularManga(page: Int, perPage: Int): Result<List<PopularManga>> {
        return try {
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val data = dataSource.getPopularManga(
                page = page,
                perPage = perPage,
                isAdult = isAdultFilter
            )

            val manga = data.Page?.media
                ?.filterNotNull()
                ?.filter { !(it.isAdult ?: false) }
                ?.mapNotNull { it.toPopularManga() }
                ?: emptyList()

            Result.success(manga)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
