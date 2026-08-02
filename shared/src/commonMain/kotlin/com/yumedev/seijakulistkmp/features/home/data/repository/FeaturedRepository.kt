package com.yumedev.seijakulistkmp.features.home.data.repository

import com.yumedev.seijakulistkmp.core.common.resource.Result
import com.yumedev.seijakulistkmp.core.common.util.safeCall
import com.yumedev.seijakulistkmp.features.home.data.datasource.FeaturedDataSource
import com.yumedev.seijakulistkmp.features.home.data.dto.FeaturedAnimeDto
import com.yumedev.seijakulistkmp.features.home.data.mapper.toFeaturedAnimeList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repositorio específico para Featured Carousel
 * Flujo aislado que no se mezcla con otras secciones del Home
 */
interface FeaturedRepository {
    fun getFeaturedAnime(perPage: Int): Flow<Result<List<FeaturedAnimeDto>>>
}

class FeaturedRepositoryImpl(
    private val dataSource: FeaturedDataSource
) : FeaturedRepository {

    override fun getFeaturedAnime(perPage: Int): Flow<Result<List<FeaturedAnimeDto>>> = flow {
        val result = safeCall {
            val data = dataSource.getFeaturedAnime(page = 1, perPage = perPage)
            data.toFeaturedAnimeList()
        }
        emit(result)
    }
}
