package com.yumedev.seijakulistkmp.features.tracking.data.mapper

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.data.local.entity.MediaListEntryEntity
import com.yumedev.seijakulistkmp.features.tracking.domain.model.CachedMediaInfo
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus

fun MediaListEntryEntity.toDomain(): MediaListEntry {
    return MediaListEntry(
        id = id,
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
        isSynced = isSynced,
        needsSync = needsSync,
        mediaInfo = if (mediaTitle != null) {
            CachedMediaInfo(
                title = mediaTitle,
                coverImage = mediaCoverImage,
                totalEpisodes = mediaTotalEpisodes,
                totalChapters = mediaTotalChapters,
                totalVolumes = mediaTotalVolumes
            )
        } else null
    )
}

fun MediaListEntry.toEntity(): MediaListEntryEntity {
    return MediaListEntryEntity(
        id = id,
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
        isSynced = isSynced,
        needsSync = needsSync,
        mediaTitle = mediaInfo?.title,
        mediaCoverImage = mediaInfo?.coverImage,
        mediaTotalEpisodes = mediaInfo?.totalEpisodes,
        mediaTotalChapters = mediaInfo?.totalChapters,
        mediaTotalVolumes = mediaInfo?.totalVolumes
    )
}
