package com.yumedev.seijakulistkmp.features.tracking.domain.repository

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.model.CachedMediaInfo
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListSortOption
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStats
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import kotlinx.coroutines.flow.Flow

interface MediaListRepository {
    suspend fun addToList(
        mediaId: Int,
        mediaType: MediaType,
        status: MediaListStatus,
        mediaInfo: CachedMediaInfo
    ): Result<MediaListEntry>

    suspend fun updateEntry(
        mediaId: Int,
        mediaType: MediaType,
        status: MediaListStatus? = null,
        progress: Int? = null,
        progressVolumes: Int? = null,
        score: Float? = null,
        startDate: String? = null,
        finishDate: String? = null,
        notes: String? = null,
        repeatCount: Int? = null,
        priority: MediaListPriority? = null
    ): Result<MediaListEntry>

    suspend fun removeFromList(mediaId: Int, mediaType: MediaType): Result<Unit>

    suspend fun getEntry(mediaId: Int, mediaType: MediaType): Result<MediaListEntry?>

    fun observeEntry(mediaId: Int, mediaType: MediaType): Flow<MediaListEntry?>

    fun observeList(
        mediaType: MediaType,
        status: MediaListStatus? = null
    ): Flow<List<MediaListEntry>>

    fun observeFilteredList(
        mediaType: MediaType,
        statuses: List<MediaListStatus>,
        sortBy: MediaListSortOption = MediaListSortOption.UPDATED_AT,
        ascending: Boolean = false
    ): Flow<List<MediaListEntry>>

    fun searchList(query: String): Flow<List<MediaListEntry>>

    fun observeStats(mediaType: MediaType): Flow<MediaListStats>

    suspend fun importFromMAL(xmlContent: String, mediaType: MediaType): Result<Int>
    suspend fun exportToMAL(mediaType: MediaType): Result<String>

    suspend fun syncWithAniList(): Result<Unit>
    suspend fun getUnsyncedEntries(): List<MediaListEntry>
    suspend fun getAllEntries(): List<MediaListEntry>
}
