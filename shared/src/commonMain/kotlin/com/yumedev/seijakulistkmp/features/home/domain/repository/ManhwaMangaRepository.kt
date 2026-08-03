package com.yumedev.seijakulistkmp.features.home.domain.repository

import com.yumedev.seijakulistkmp.features.home.domain.model.ManhwaManga

interface ManhwaMangaRepository {
    suspend fun getManhwaManga(page: Int = 1, perPage: Int = 10, countryOfOrigin: String = "KR"): Result<List<ManhwaManga>>
}
