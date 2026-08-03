package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.TopRatedMangaRepository

class GetTopRatedMangaUseCase(
    private val repository: TopRatedMangaRepository
) {
    suspend operator fun invoke(page: Int = 1, perPage: Int = 10): Result<List<TopRatedManga>> {
        return repository.getTopRatedManga(page, perPage)
    }
}
