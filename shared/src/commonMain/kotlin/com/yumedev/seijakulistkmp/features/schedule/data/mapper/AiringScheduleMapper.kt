package com.yumedev.seijakulistkmp.features.schedule.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetWeeklyAiringScheduleQuery
import com.yumedev.seijakulistkmp.features.schedule.data.local.entity.AiringScheduleEntity
import com.yumedev.seijakulistkmp.features.schedule.data.local.entity.AiringScheduleWithStatus
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus

fun GetWeeklyAiringScheduleQuery.AiringSchedule.toDomain(): AiringScheduleItem? {
    val mediaData = media ?: return null

    val displayTitle = mediaData.title?.romaji ?: mediaData.title?.english ?: mediaData.title?.native ?: return null
    val coverUrl = mediaData.coverImage?.large ?: mediaData.coverImage?.medium

    return AiringScheduleItem(
        id = id,
        mediaId = mediaId,
        mediaTitle = displayTitle,
        mediaTitleEnglish = mediaData.title?.english,
        mediaTitleNative = mediaData.title?.native,
        coverImageUrl = coverUrl,
        coverImageColor = mediaData.coverImage?.color,
        episode = episode,
        airingAt = airingAt.toLong(),
        timeUntilAiring = timeUntilAiring.toLong(),
        genres = mediaData.genres?.filterNotNull() ?: emptyList(),
        averageScore = mediaData.averageScore,
        format = mediaData.format?.rawValue,
        status = mediaData.status?.rawValue,
        totalEpisodes = mediaData.episodes
    )
}

fun GetWeeklyAiringScheduleQuery.AiringSchedule.toEntity(weekStart: Long, cachedAt: Long): AiringScheduleEntity? {
    val mediaData = media ?: return null

    val displayTitle = mediaData.title?.romaji ?: mediaData.title?.english ?: mediaData.title?.native ?: return null
    val genresJson = (mediaData.genres?.filterNotNull() ?: emptyList()).joinToString(",")

    return AiringScheduleEntity(
        id = id,
        mediaId = mediaId,
        mediaTitleRomaji = displayTitle,
        mediaTitleEnglish = mediaData.title?.english,
        mediaTitleNative = mediaData.title?.native,
        coverImageUrl = mediaData.coverImage?.large ?: mediaData.coverImage?.medium,
        coverImageColor = mediaData.coverImage?.color,
        episode = episode,
        airingAt = airingAt.toLong(),
        timeUntilAiring = timeUntilAiring.toLong(),
        genres = genresJson,
        averageScore = mediaData.averageScore,
        format = mediaData.format?.rawValue,
        status = mediaData.status?.rawValue,
        totalEpisodes = mediaData.episodes,
        cachedAt = cachedAt,
        weekStart = weekStart
    )
}

fun AiringScheduleEntity.toDomain(): AiringScheduleItem {
    return AiringScheduleItem(
        id = id,
        mediaId = mediaId,
        mediaTitle = mediaTitleRomaji,
        mediaTitleEnglish = mediaTitleEnglish,
        mediaTitleNative = mediaTitleNative,
        coverImageUrl = coverImageUrl,
        coverImageColor = coverImageColor,
        episode = episode,
        airingAt = airingAt,
        timeUntilAiring = timeUntilAiring,
        genres = if (genres.isNotEmpty()) genres.split(",") else emptyList(),
        averageScore = averageScore,
        format = format,
        status = status,
        totalEpisodes = totalEpisodes
    )
}

fun AiringScheduleWithStatus.toDomain(): AiringScheduleItem {
    val userStatus = userListStatus?.let {
        try {
            MediaListStatus.valueOf(it)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    return AiringScheduleItem(
        id = schedule.id,
        mediaId = schedule.mediaId,
        mediaTitle = schedule.mediaTitleRomaji,
        mediaTitleEnglish = schedule.mediaTitleEnglish,
        mediaTitleNative = schedule.mediaTitleNative,
        coverImageUrl = schedule.coverImageUrl,
        coverImageColor = schedule.coverImageColor,
        episode = schedule.episode,
        airingAt = schedule.airingAt,
        timeUntilAiring = schedule.timeUntilAiring,
        genres = if (schedule.genres.isNotEmpty()) schedule.genres.split(",") else emptyList(),
        averageScore = schedule.averageScore,
        format = schedule.format,
        status = schedule.status,
        totalEpisodes = schedule.totalEpisodes,
        userListStatus = userStatus
    )
}

@JvmName("airingScheduleQueryToItems")
fun List<GetWeeklyAiringScheduleQuery.AiringSchedule?>.toAiringScheduleItems(): List<AiringScheduleItem> {
    return this.filterNotNull()
        .mapNotNull { it.toDomain() }
}

fun List<GetWeeklyAiringScheduleQuery.AiringSchedule?>.toAiringScheduleEntities(weekStart: Long, cachedAt: Long): List<AiringScheduleEntity> {
    return this.filterNotNull()
        .mapNotNull { it.toEntity(weekStart, cachedAt) }
}

@JvmName("airingScheduleEntityToItems")
fun List<AiringScheduleEntity>.toAiringScheduleItems(): List<AiringScheduleItem> {
    return this.map { it.toDomain() }
}

@JvmName("airingScheduleWithStatusToItems")
fun List<AiringScheduleWithStatus>.toAiringScheduleItems(): List<AiringScheduleItem> {
    return this.map { it.toDomain() }
}
