package com.yumedev.seijakulistkmp.features.home.domain.repository

import com.yumedev.seijakulistkmp.features.home.domain.model.NextSeasonAnime

interface NextSeasonRepository {
    suspend fun getNextSeasonAnime(page: Int = 1, perPage: Int = 10): Result<List<NextSeasonAnime>>
}
