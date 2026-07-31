package com.yumedev.seijakulistkmp.features.home.presentation.model

data class FeaturedMediaItem(
    val id: Int,
    val title: String,
    val coverImageUrl: String?,
    val rating: String?,
    val status: String?,
    val metadata: String
)
