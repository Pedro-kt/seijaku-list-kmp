package com.yumedev.seijakulistkmp.features.home.domain.repository

import com.yumedev.seijakulistkmp.features.home.domain.model.FeaturedManga

interface FeaturedMangaRepository {
    suspend fun getFeaturedManga(page: Int = 1, perPage: Int = 5): Result<List<FeaturedManga>>
}
