package com.yumedev.seijakulistkmp.features.home.domain.repository

import com.yumedev.seijakulistkmp.features.home.domain.model.PublishingManga

interface PublishingMangaRepository {
    suspend fun getPublishingManga(page: Int = 1, perPage: Int = 10): Result<List<PublishingManga>>
}
