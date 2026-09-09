package com.yumedev.seijakulistkmp.features.home.data.repository

import com.yumedev.seijakulistkmp.features.home.data.datasource.ManhwaMangaDataSource
import com.yumedev.seijakulistkmp.features.home.data.mapper.toManhwaManga
import com.yumedev.seijakulistkmp.features.home.domain.model.ManhwaManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.ManhwaMangaRepository
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class ManhwaMangaRepositoryImpl(
    private val dataSource: ManhwaMangaDataSource,
    private val settingsRepository: SettingsRepository
) : ManhwaMangaRepository {

    override suspend fun getManhwaManga(page: Int, perPage: Int, countryOfOrigin: String): Result<List<ManhwaManga>> {
        return try {
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val data = dataSource.getManhwaManga(
                page = page,
                perPage = perPage,
                countryOfOrigin = countryOfOrigin,
                isAdult = isAdultFilter
            )

            val manga = data.Page?.media
                ?.filterNotNull()
                ?.filter { !(it.isAdult ?: false) }
                ?.mapNotNull { it.toManhwaManga() }
                ?: emptyList()

            Result.success(manga)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
