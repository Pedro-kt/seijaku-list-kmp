package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.home.domain.model.RecentlyAddedManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.RecentlyAddedMangaRepository

class GetRecentlyAddedMangaUseCase(
    private val repository: RecentlyAddedMangaRepository
) {
    suspend operator fun invoke(page: Int = 1, perPage: Int = 10): Result<List<RecentlyAddedManga>> {
        return repository.getRecentlyAddedManga(page, perPage)
    }
}
