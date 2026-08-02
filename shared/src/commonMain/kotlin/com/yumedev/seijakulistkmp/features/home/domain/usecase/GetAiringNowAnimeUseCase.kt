package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.home.domain.model.AiringNowAnime
import com.yumedev.seijakulistkmp.features.home.domain.repository.AiringNowRepository

class GetAiringNowAnimeUseCase(
    private val repository: AiringNowRepository
) {
    suspend operator fun invoke(page: Int = 1, perPage: Int = 10): Result<List<AiringNowAnime>> {
        return repository.getAiringNowAnime(page, perPage)
    }
}
