package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository

class ExportToMALUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(mediaType: MediaType): Result<String> {
        return repository.exportToMAL(mediaType)
    }
}
