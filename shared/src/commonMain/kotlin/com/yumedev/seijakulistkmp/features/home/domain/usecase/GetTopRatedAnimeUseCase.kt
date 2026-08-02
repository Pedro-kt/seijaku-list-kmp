package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedAnime
import com.yumedev.seijakulistkmp.features.home.domain.repository.TopRatedRepository

class GetTopRatedAnimeUseCase(
    private val repository: TopRatedRepository
) {
    suspend operator fun invoke(page: Int = 1, perPage: Int = 10): Result<List<TopRatedAnime>> {
        return repository.getTopRatedAnime(page, perPage)
    }
}
