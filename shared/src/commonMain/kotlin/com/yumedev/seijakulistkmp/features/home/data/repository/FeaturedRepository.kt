package com.yumedev.seijakulistkmp.features.home.data.repository

import com.yumedev.seijakulistkmp.core.common.resource.Result
import com.yumedev.seijakulistkmp.core.common.util.safeCall
import com.yumedev.seijakulistkmp.features.home.data.datasource.FeaturedDataSource
import com.yumedev.seijakulistkmp.features.home.data.dto.FeaturedAnimeDto
import com.yumedev.seijakulistkmp.features.home.data.mapper.toFeaturedAnimeList
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

/**
 * Repositorio específico para Featured Carousel
 * Flujo aislado que no se mezcla con otras secciones del Home
 */
interface FeaturedRepository {
    fun getFeaturedAnime(perPage: Int): Flow<Result<List<FeaturedAnimeDto>>>
}

class FeaturedRepositoryImpl(
    private val dataSource: FeaturedDataSource,
    private val settingsRepository: SettingsRepository
) : FeaturedRepository {

    override fun getFeaturedAnime(perPage: Int): Flow<Result<List<FeaturedAnimeDto>>> = flow {
        val result = safeCall {
            val sfwModeEnabled = settingsRepository.getSfwMode().firstOrNull() ?: true
            val isAdultFilter = if (sfwModeEnabled) false else null

            val data = dataSource.getFeaturedAnime(
                page = 1,
                perPage = perPage,
                isAdult = isAdultFilter
            )
            data.toFeaturedAnimeList()
        }
        emit(result)
    }
}
