package com.yumedev.seijakulistkmp.features.tracking.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MediaListEntryDto(
    val id: String = "",
    val mediaId: Int = 0,
    val mediaType: String = "",
    val status: String = "",
    val progress: Int = 0,
    val progressVolumes: Int? = null,
    val repeatCount: Int = 0,
    val score: Float? = null,
    val startDate: String? = null,
    val finishDate: String? = null,
    val notes: String? = null,
    val priority: Int = 0,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val anilistEntryId: Int? = null,
    val mediaTitle: String? = null,
    val mediaCoverImage: String? = null,
    val mediaTotalEpisodes: Int? = null,
    val mediaTotalChapters: Int? = null,
    val mediaTotalVolumes: Int? = null,
    val mediaStatus: String? = null
)
