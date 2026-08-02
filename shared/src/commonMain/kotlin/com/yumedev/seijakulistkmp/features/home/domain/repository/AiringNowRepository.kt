package com.yumedev.seijakulistkmp.features.home.domain.repository

import com.yumedev.seijakulistkmp.features.home.domain.model.AiringNowAnime

interface AiringNowRepository {
    suspend fun getAiringNowAnime(page: Int = 1, perPage: Int = 10): Result<List<AiringNowAnime>>
}
