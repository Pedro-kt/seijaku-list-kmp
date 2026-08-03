package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.home.domain.model.ManhwaManga
import com.yumedev.seijakulistkmp.features.home.domain.repository.ManhwaMangaRepository

class GetManhwaMangaUseCase(
    private val repository: ManhwaMangaRepository
) {
    suspend operator fun invoke(page: Int = 1, perPage: Int = 10, countryOfOrigin: String = "KR"): Result<List<ManhwaManga>> {
        return repository.getManhwaManga(page, perPage, countryOfOrigin)
    }
}
