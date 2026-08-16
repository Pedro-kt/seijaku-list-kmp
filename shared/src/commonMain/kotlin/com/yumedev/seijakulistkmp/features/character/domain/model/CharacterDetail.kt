package com.yumedev.seijakulistkmp.features.character.domain.model

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType

data class CharacterDetail(
    val id: Int,
    val name: String,
    val nameNative: String?,
    val alternativeNames: List<String>,
    val alternativeSpoilerNames: List<String>,
    val imageUrl: String?,
    val description: String?,
    val gender: String?,
    val dateOfBirth: String?,
    val age: String?,
    val bloodType: String?,
    val isFavourite: Boolean,
    val favourites: Int?,
    val siteUrl: String?,
    val media: List<CharacterMedia>,
    val voiceActors: List<VoiceActor>
)

data class CharacterMedia(
    val id: Int,
    val title: String,
    val titleNative: String?,
    val coverImageUrl: String?,
    val type: MediaType,
    val format: String?,
    val status: String?,
    val averageScore: Double?,
    val popularity: Int?,
    val startDate: String?,
    val characterRole: String,
    val voiceActorsForMedia: List<VoiceActor>
)

data class VoiceActor(
    val id: Int,
    val name: String,
    val nameNative: String?,
    val imageUrl: String?,
    val language: String?,
    val roleNotes: String?
)
