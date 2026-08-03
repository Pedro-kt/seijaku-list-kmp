package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.home.domain.model.PopularManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.PopularMangaRepository

class GetPopularMangaUseCase(
    private val repository: PopularMangaRepository
) {
    suspend operator fun invoke(page: Int = 1, perPage: Int = 10): Result<List<PopularManga>> {
        return repository.getPopularManga(page, perPage)
    }
}
