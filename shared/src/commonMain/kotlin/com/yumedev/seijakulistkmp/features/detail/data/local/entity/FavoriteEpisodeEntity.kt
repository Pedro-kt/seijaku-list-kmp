package com.yumedev.seijakulistkmp.features.detail.data.local.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "favorite_episodes",
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["media_id"]),
        Index(value = ["user_id", "media_id", "episode_number"], unique = true)
    ]
)
data class FavoriteEpisodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "media_id")
    val mediaId: Int,

    @ColumnInfo(name = "episode_number")
    val episodeNumber: Int,

    @ColumnInfo(name = "episode_title")
    val episodeTitle: String,

    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String?,

    @ColumnInfo(name = "marked_at")
    val markedAt: Long,

    @ColumnInfo(name = "synced_at")
    val syncedAt: Long? = null
)
