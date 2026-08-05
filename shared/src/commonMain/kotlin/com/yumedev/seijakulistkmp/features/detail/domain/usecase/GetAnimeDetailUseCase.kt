package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.detail.domain.repository.MediaDetailRepository

class GetAnimeDetailUseCase(
    private val repository: MediaDetailRepository
) {
    suspend operator fun invoke(id: Int): Result<MediaDetail> {
        return repository.getAnimeDetail(id)
    }
}
