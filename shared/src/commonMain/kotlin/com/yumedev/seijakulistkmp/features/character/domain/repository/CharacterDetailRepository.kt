package com.yumedev.seijakulistkmp.features.character.domain.repository

import com.yumedev.seijakulistkmp.features.character.domain.model.CharacterDetail

interface CharacterDetailRepository {
    suspend fun getCharacterDetail(id: Int): Result<CharacterDetail>
}
