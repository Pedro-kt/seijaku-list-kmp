package com.yumedev.seijakulistkmp.features.character.domain.usecase

import com.yumedev.seijakulistkmp.features.character.domain.model.CharacterDetail
import com.yumedev.seijakulistkmp.features.character.domain.repository.CharacterDetailRepository

class GetCharacterDetailUseCase(
    private val repository: CharacterDetailRepository
) {
    suspend operator fun invoke(id: Int): Result<CharacterDetail> {
        return repository.getCharacterDetail(id)
    }
}
