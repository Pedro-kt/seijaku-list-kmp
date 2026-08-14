package com.yumedev.seijakulistkmp.features.character.presentation

import com.yumedev.seijakulistkmp.core.error.ErrorType
import com.yumedev.seijakulistkmp.features.character.domain.model.CharacterDetail

data class CharacterState(
    val characterDetail: CharacterDetail? = null,
    val isLoading: Boolean = false,
    val error: ErrorType? = null
)
