package com.yumedev.seijakulistkmp.features.tracking.domain.model

import com.yumedev.seijakulistkmp.core.domain.model.MediaType

data class MediaListEntry(
    val id: Long,
    val mediaId: Int,
    val mediaType: MediaType,
    val status: MediaListStatus,
    val progress: Int,
    val progressVolumes: Int?,
    val repeatCount: Int,
    val score: Float?,
    val startDate: String?,
    val finishDate: String?,
    val notes: String?,
    val priority: MediaListPriority,
    val createdAt: Long,
    val updatedAt: Long,
    val mediaInfo: CachedMediaInfo?,
    // Sync fields
    val anilistEntryId: Int? = null,
    val isSynced: Boolean = false,
    val needsSync: Boolean = false
)
