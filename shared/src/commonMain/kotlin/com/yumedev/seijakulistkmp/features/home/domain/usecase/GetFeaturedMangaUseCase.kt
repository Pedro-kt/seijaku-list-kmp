package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.home.domain.model.FeaturedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.FeaturedMangaRepository

class GetFeaturedMangaUseCase(
    private val repository: FeaturedMangaRepository
) {
    suspend operator fun invoke(page: Int = 1, perPage: Int = 5): Result<List<FeaturedManga>> {
        return repository.getFeaturedManga(page, perPage)
    }
}
