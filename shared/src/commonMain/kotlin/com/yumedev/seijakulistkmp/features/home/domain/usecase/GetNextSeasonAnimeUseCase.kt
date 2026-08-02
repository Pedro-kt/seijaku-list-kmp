package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.home.domain.model.NextSeasonAnime
import com.yumedev.seijakulistkmp.features.home.domain.repository.NextSeasonRepository

class GetNextSeasonAnimeUseCase(
    private val repository: NextSeasonRepository
) {
    suspend operator fun invoke(page: Int = 1, perPage: Int = 10): Result<List<NextSeasonAnime>> {
        return repository.getNextSeasonAnime(page, perPage)
    }
}
