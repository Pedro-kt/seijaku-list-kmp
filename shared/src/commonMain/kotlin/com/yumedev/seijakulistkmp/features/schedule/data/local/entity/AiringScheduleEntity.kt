package com.yumedev.seijakulistkmp.features.schedule.data.local.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "airing_schedules",
    indices = [
        Index(value = ["week_start"]),
        Index(value = ["airing_at"])
    ]
)
data class AiringScheduleEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "media_id")
    val mediaId: Int,
    @ColumnInfo(name = "media_title_romaji")
    val mediaTitleRomaji: String,
    @ColumnInfo(name = "media_title_english")
    val mediaTitleEnglish: String?,
    @ColumnInfo(name = "media_title_native")
    val mediaTitleNative: String?,
    @ColumnInfo(name = "cover_image_url")
    val coverImageUrl: String?,
    @ColumnInfo(name = "cover_image_color")
    val coverImageColor: String?,
    @ColumnInfo(name = "episode")
    val episode: Int,
    @ColumnInfo(name = "airing_at")
    val airingAt: Long,
    @ColumnInfo(name = "time_until_airing")
    val timeUntilAiring: Long,
    @ColumnInfo(name = "genres")
    val genres: String,
    @ColumnInfo(name = "average_score")
    val averageScore: Int?,
    @ColumnInfo(name = "format")
    val format: String?,
    @ColumnInfo(name = "status")
    val status: String?,
    @ColumnInfo(name = "cached_at")
    val cachedAt: Long,
    @ColumnInfo(name = "week_start")
    val weekStart: Long
)
