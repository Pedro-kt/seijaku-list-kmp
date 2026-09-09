package com.yumedev.seijakulistkmp.features.home.data.repository

import com.yumedev.seijakulistkmp.features.home.data.datasource.FeaturedMangaDataSource
import com.yumedev.seijakulistkmp.features.home.data.mapper.toFeaturedManga
import com.yumedev.seijakulistkmp.features.home.domain.model.FeaturedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.FeaturedMangaRepository
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class FeaturedMangaRepositoryImpl(
    private val dataSource: FeaturedMangaDataSource,
    private val settingsRepository: SettingsRepository
) : FeaturedMangaRepository {

    override suspend fun getFeaturedManga(page: Int, perPage: Int): Result<List<FeaturedManga>> {
        return try {
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val data = dataSource.getFeaturedManga(
                page = page,
                perPage = perPage,
                isAdult = isAdultFilter
            )

            val manga = data.Page?.media
                ?.filterNotNull()
                ?.filter { !(it.isAdult ?: false) }
                ?.mapNotNull { it.toFeaturedManga() }
                ?: emptyList()

            Result.success(manga)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
