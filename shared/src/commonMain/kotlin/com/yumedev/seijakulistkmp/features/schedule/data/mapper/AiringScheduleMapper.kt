package com.yumedev.seijakulistkmp.features.schedule.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetWeeklyAiringScheduleQuery
import com.yumedev.seijakulistkmp.features.schedule.data.local.entity.AiringScheduleEntity
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem

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
        status = mediaData.status?.rawValue
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
        status = status
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
