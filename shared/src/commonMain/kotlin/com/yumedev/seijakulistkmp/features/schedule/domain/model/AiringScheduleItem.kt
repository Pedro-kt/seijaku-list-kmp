package com.yumedev.seijakulistkmp.features.schedule.domain.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime

data class AiringScheduleItem(
    val id: Int,
    val mediaId: Int,
    val mediaTitle: String,
    val mediaTitleEnglish: String?,
    val mediaTitleNative: String?,
    val coverImageUrl: String?,
    val coverImageColor: String?,
    val episode: Int,
    val airingAt: Long,
    val timeUntilAiring: Long,
    val genres: List<String>,
    val averageScore: Int?,
    val format: String?,
    val status: String?
)
