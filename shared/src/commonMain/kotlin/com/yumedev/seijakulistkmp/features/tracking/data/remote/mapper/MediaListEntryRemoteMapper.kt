package com.yumedev.seijakulistkmp.features.tracking.data.remote.mapper

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.data.remote.dto.MediaListEntryDto
import com.yumedev.seijakulistkmp.features.tracking.domain.model.CachedMediaInfo
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus

fun MediaListEntry.toDto(firestoreId: String): MediaListEntryDto {
    return MediaListEntryDto(
        id = firestoreId,
        mediaId = mediaId,
        mediaType = mediaType.name,
        status = status.name,
        progress = progress,
        progressVolumes = progressVolumes,
        repeatCount = repeatCount,
        score = score,
        startDate = startDate,
        finishDate = finishDate,
        notes = notes,
        priority = priority.value,
        createdAt = createdAt,
        updatedAt = updatedAt,
        anilistEntryId = anilistEntryId,
        favoritePosition = favoritePosition,
        mediaTitle = mediaInfo?.title,
        mediaCoverImage = mediaInfo?.coverImage,
        mediaTotalEpisodes = mediaInfo?.totalEpisodes,
        mediaTotalChapters = mediaInfo?.totalChapters,
        mediaTotalVolumes = mediaInfo?.totalVolumes,
        mediaStatus = mediaStatus
    )
}

fun MediaListEntryDto.toDomain(localId: Long): MediaListEntry {
    return MediaListEntry(
        id = localId,
        mediaId = mediaId,
        mediaType = MediaType.valueOf(mediaType),
        status = MediaListStatus.valueOf(status),
        progress = progress,
        progressVolumes = progressVolumes,
        repeatCount = repeatCount,
        score = score,
        startDate = startDate,
        finishDate = finishDate,
        notes = notes,
        priority = MediaListPriority.fromValue(priority),
        createdAt = createdAt,
        updatedAt = updatedAt,
        anilistEntryId = anilistEntryId,
        favoritePosition = favoritePosition,
        isSynced = true,
        needsSync = false,
        mediaInfo = if (mediaTitle != null) {
            CachedMediaInfo(
                title = mediaTitle,
                coverImage = mediaCoverImage,
                totalEpisodes = mediaTotalEpisodes,
                totalChapters = mediaTotalChapters,
                totalVolumes = mediaTotalVolumes,
                mediaStatus = mediaStatus
            )
        } else null,
        mediaStatus = mediaStatus
    )
}
