package com.yumedev.seijakulistkmp.features.home.domain.repository

import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedManga

interface TopRatedMangaRepository {
    suspend fun getTopRatedManga(page: Int = 1, perPage: Int = 10): Result<List<TopRatedManga>>
}
