package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository

class ImportFromMALUseCase(
    private val repository: MediaListRepository
) {
    suspend operator fun invoke(xmlContent: String, mediaType: MediaType): Result<Int> {
        return repository.importFromMAL(xmlContent, mediaType)
    }
}
