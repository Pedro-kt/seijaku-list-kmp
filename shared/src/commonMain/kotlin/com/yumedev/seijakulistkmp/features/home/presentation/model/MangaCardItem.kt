package com.yumedev.seijakulistkmp.features.home.presentation.model

data class MangaCardItem(
    val id: Int,
    val title: String,
    val coverImageUrl: String?,
    val rating: String?,
    val format: String?,
    val chapters: String?,
    val volumes: String?,
    val genres: List<String>
) {
    val volumesOrChapters: String?
        get() = volumes ?: chapters
}
