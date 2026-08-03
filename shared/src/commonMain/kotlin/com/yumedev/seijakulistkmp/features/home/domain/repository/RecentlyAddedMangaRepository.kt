package com.yumedev.seijakulistkmp.features.home.domain.repository

import com.yumedev.seijakulistkmp.features.home.domain.model.RecentlyAddedManga

interface RecentlyAddedMangaRepository {
    suspend fun getRecentlyAddedManga(page: Int = 1, perPage: Int = 10): Result<List<RecentlyAddedManga>>
}
