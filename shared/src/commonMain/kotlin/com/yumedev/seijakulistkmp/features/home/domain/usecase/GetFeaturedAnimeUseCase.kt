package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.core.common.resource.Resource
import com.yumedev.seijakulistkmp.core.common.resource.Result
import com.yumedev.seijakulistkmp.core.common.resource.toResource
import com.yumedev.seijakulistkmp.features.home.data.dto.FeaturedAnimeDto
import com.yumedev.seijakulistkmp.features.home.data.repository.FeaturedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case específico para obtener anime destacado del carousel
 * Usa el flujo aislado de FeaturedRepository
 */
class GetFeaturedAnimeUseCase(
    private val repository: FeaturedRepository
) {
    operator fun invoke(limit: Int = 5): Flow<Resource<List<FeaturedAnimeDto>>> = flow {
        emit(Resource.Loading)

        repository.getFeaturedAnime(perPage = limit).collect { result ->
            when (result) {
                is Result.Success -> {
                    emit(Resource.Success(result.data))
                }
                is Result.Failure -> {
                    emit(Resource.Error(result.error.message, result.error.toException()))
                }
            }
        }
    }
}
