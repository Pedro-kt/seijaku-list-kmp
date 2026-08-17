package com.yumedev.seijakulistkmp.features.tracking.data.local.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "media_list_entries",
    indices = [
        Index(value = ["media_id", "media_type"], unique = true),
        Index(value = ["status"]),
        Index(value = ["media_type"]),
        Index(value = ["updated_at"])
    ]
)
data class MediaListEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "media_id")
    val mediaId: Int,

    @ColumnInfo(name = "media_type")
    val mediaType: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "progress")
    val progress: Int = 0,

    @ColumnInfo(name = "progress_volumes")
    val progressVolumes: Int? = null,

    @ColumnInfo(name = "repeat_count")
    val repeatCount: Int = 0,

    @ColumnInfo(name = "score")
    val score: Float? = null,

    @ColumnInfo(name = "start_date")
    val startDate: String? = null,

    @ColumnInfo(name = "finish_date")
    val finishDate: String? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "priority")
    val priority: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),

    @ColumnInfo(name = "anilist_entry_id")
    val anilistEntryId: Int? = null,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "needs_sync")
    val needsSync: Boolean = false,

    @ColumnInfo(name = "media_title")
    val mediaTitle: String? = null,

    @ColumnInfo(name = "media_cover_image")
    val mediaCoverImage: String? = null,

    @ColumnInfo(name = "media_total_episodes")
    val mediaTotalEpisodes: Int? = null,

    @ColumnInfo(name = "media_total_chapters")
    val mediaTotalChapters: Int? = null,

    @ColumnInfo(name = "media_total_volumes")
    val mediaTotalVolumes: Int? = null
)
