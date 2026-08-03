package com.yumedev.seijakulistkmp.features.home.domain.repository

import com.yumedev.seijakulistkmp.features.home.domain.model.PopularManga

interface PopularMangaRepository {
    suspend fun getPopularManga(page: Int = 1, perPage: Int = 10): Result<List<PopularManga>>
}
