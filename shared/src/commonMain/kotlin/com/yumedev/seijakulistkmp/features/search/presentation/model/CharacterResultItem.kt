package com.yumedev.seijakulistkmp.features.search.presentation.model

data class CharacterResultItem(
    val id: Int,
    val name: String,
    val image: String,
    val mediaAppearances: List<MediaAppearance> = emptyList()
)

data class MediaAppearance(
    val mediaId: Int,
    val mediaTitle: String,
    val role: String? = null
)
