package com.yumedev.seijakulistkmp.features.home.data.repository

import com.yumedev.seijakulistkmp.features.home.data.datasource.TopRatedMangaDataSource
import com.yumedev.seijakulistkmp.features.home.data.mapper.toTopRatedManga
import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.TopRatedMangaRepository
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class TopRatedMangaRepositoryImpl(
    private val dataSource: TopRatedMangaDataSource,
    private val settingsRepository: SettingsRepository
) : TopRatedMangaRepository {

    override suspend fun getTopRatedManga(page: Int, perPage: Int): Result<List<TopRatedManga>> {
        return try {
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val data = dataSource.getTopRatedManga(
                page = page,
                perPage = perPage,
                isAdult = isAdultFilter
            )

            val manga = data.Page?.media
                ?.filterNotNull()
                ?.filter { !(it.isAdult ?: false) }
                ?.mapNotNull { it.toTopRatedManga() }
                ?: emptyList()

            Result.success(manga)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
