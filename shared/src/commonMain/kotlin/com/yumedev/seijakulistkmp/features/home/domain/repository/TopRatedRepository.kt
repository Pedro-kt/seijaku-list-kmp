package com.yumedev.seijakulistkmp.features.home.domain.repository

import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedAnime

interface TopRatedRepository {
    suspend fun getTopRatedAnime(page: Int = 1, perPage: Int = 10): Result<List<TopRatedAnime>>
}
