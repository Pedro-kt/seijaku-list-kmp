package com.yumedev.seijakulistkmp.features.home.data.repository

import com.yumedev.seijakulistkmp.features.home.data.datasource.RecentlyAddedMangaDataSource
import com.yumedev.seijakulistkmp.features.home.data.mapper.toRecentlyAddedManga
import com.yumedev.seijakulistkmp.features.home.domain.model.RecentlyAddedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.RecentlyAddedMangaRepository
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class RecentlyAddedMangaRepositoryImpl(
    private val dataSource: RecentlyAddedMangaDataSource,
    private val settingsRepository: SettingsRepository
) : RecentlyAddedMangaRepository {

    override suspend fun getRecentlyAddedManga(page: Int, perPage: Int): Result<List<RecentlyAddedManga>> {
        return try {
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val data = dataSource.getRecentlyAddedManga(
                page = page,
                perPage = perPage,
                isAdult = isAdultFilter
            )

            val manga = data.Page?.media
                ?.filterNotNull()
                ?.filter { !(it.isAdult ?: false) }
                ?.mapNotNull { it.toRecentlyAddedManga() }
                ?: emptyList()

            Result.success(manga)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
