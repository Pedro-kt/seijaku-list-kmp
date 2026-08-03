package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.home.domain.model.PublishingManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.PublishingMangaRepository

class GetPublishingMangaUseCase(
    private val repository: PublishingMangaRepository
) {
    suspend operator fun invoke(page: Int = 1, perPage: Int = 10): Result<List<PublishingManga>> {
        return repository.getPublishingManga(page, perPage)
    }
}
