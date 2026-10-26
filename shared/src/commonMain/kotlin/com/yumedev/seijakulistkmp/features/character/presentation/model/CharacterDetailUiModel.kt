package com.yumedev.seijakulistkmp.features.character.presentation.model

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType

data class CharacterDetailUiModel(
    val id: Int,
    val name: String,
    val nameNative: String?,
    val imageUrl: String?,
    val roleLabel: String?,
    val formattedFavorites: String?,
    val formattedAppearances: String?,
    val formattedRanking: String?,
    val age: String?,
    val height: String?,
    val birthday: String?,
    val nickname: String?,
    val description: String?,
    val media: List<MediaAppearanceUiModel>,
    val voiceActors: List<VoiceActorUiModel>,
    val isFavorite: Boolean = false
)

data class MediaAppearanceUiModel(
    val id: Int,
    val title: String,
    val coverImageUrl: String?,
    val typeLabel: String,
    val roleLabel: String,
    val type: MediaType
)

data class VoiceActorUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val languageLabel: String?
)
