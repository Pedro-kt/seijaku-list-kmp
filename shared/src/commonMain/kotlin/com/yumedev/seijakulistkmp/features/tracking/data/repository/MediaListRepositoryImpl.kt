package com.yumedev.seijakulistkmp.features.tracking.data.repository

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.core.domain.model.resultOf
import com.yumedev.seijakulistkmp.features.tracking.data.export.MALXmlMapper
import com.yumedev.seijakulistkmp.features.tracking.data.local.dao.MediaListDao
import com.yumedev.seijakulistkmp.features.tracking.data.mapper.toDomain
import com.yumedev.seijakulistkmp.features.tracking.data.mapper.toEntity
import com.yumedev.seijakulistkmp.features.tracking.domain.model.CachedMediaInfo
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ConflictReason
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ConflictResolution
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ErrorReason
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportConflict
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportError
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportResult
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListSortOption
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStats
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import com.yumedev.seijakulistkmp.features.tracking.domain.validator.MediaListValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MediaListRepositoryImpl(
    private val mediaListDao: MediaListDao,
    private val malXmlMapper: MALXmlMapper
) : MediaListRepository {

    override suspend fun addToList(
        mediaId: Int,
        mediaType: MediaType,
        status: MediaListStatus,
        mediaInfo: CachedMediaInfo
    ): Result<MediaListEntry> = resultOf {
        val now = System.currentTimeMillis()
        val entity = com.yumedev.seijakulistkmp.features.tracking.data.local.entity.MediaListEntryEntity(
            mediaId = mediaId,
            mediaType = mediaType.name,
            status = status.name,
            progress = 0,
            progressVolumes = if (mediaType == MediaType.MANGA) 0 else null,
            repeatCount = 0,
            score = null,
            startDate = null,
            finishDate = null,
            notes = null,
            priority = MediaListPriority.MEDIUM.value,
            createdAt = now,
            updatedAt = now,
            needsSync = true,
            mediaTitle = mediaInfo.title,
            mediaCoverImage = mediaInfo.coverImage,
            mediaTotalEpisodes = mediaInfo.totalEpisodes,
            mediaTotalChapters = mediaInfo.totalChapters,
            mediaTotalVolumes = mediaInfo.totalVolumes,
            mediaStatus = mediaInfo.mediaStatus
        )

        val id = mediaListDao.insertEntry(entity)
        entity.copy(id = id).toDomain()
    }

    override suspend fun updateEntry(
        mediaId: Int,
        mediaType: MediaType,
        status: MediaListStatus?,
        progress: Int?,
        progressVolumes: Int?,
        score: Float?,
        startDate: String?,
        finishDate: String?,
        notes: String?,
        repeatCount: Int?,
        priority: MediaListPriority?,
        mediaStatus: String?
    ): Result<MediaListEntry> = resultOf {
        val existing = mediaListDao.getEntryByMedia(mediaId, mediaType.name)
            ?: throw IllegalStateException("Entry not found for mediaId: $mediaId")

        val updated = existing.copy(
            status = status?.name ?: existing.status,
            progress = progress ?: existing.progress,
            progressVolumes = progressVolumes ?: existing.progressVolumes,
            score = score ?: existing.score,
            startDate = startDate ?: existing.startDate,
            finishDate = finishDate ?: existing.finishDate,
            notes = notes ?: existing.notes,
            repeatCount = repeatCount ?: existing.repeatCount,
            priority = priority?.value ?: existing.priority,
            mediaStatus = mediaStatus ?: existing.mediaStatus,
            updatedAt = System.currentTimeMillis(),
            needsSync = true
        )

        mediaListDao.updateEntry(updated)
        updated.toDomain()
    }

    override suspend fun removeFromList(mediaId: Int, mediaType: MediaType): Result<Unit> = resultOf {
        mediaListDao.deleteByMedia(mediaId, mediaType.name)
    }

    override suspend fun getEntry(mediaId: Int, mediaType: MediaType): Result<MediaListEntry?> = resultOf {
        mediaListDao.getEntryByMedia(mediaId, mediaType.name)?.toDomain()
    }

    override fun observeEntry(mediaId: Int, mediaType: MediaType): Flow<MediaListEntry?> {
        return mediaListDao.observeEntryByMedia(mediaId, mediaType.name)
            .map { it?.toDomain() }
    }

    override fun observeList(
        mediaType: MediaType,
        status: MediaListStatus?
    ): Flow<List<MediaListEntry>> {
        return if (status != null) {
            mediaListDao.observeEntriesByTypeAndStatus(mediaType.name, status.name)
        } else {
            mediaListDao.observeEntriesByType(mediaType.name)
        }.map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeFilteredList(
        mediaType: MediaType,
        statuses: List<MediaListStatus>,
        sortBy: MediaListSortOption,
        ascending: Boolean
    ): Flow<List<MediaListEntry>> {
        return mediaListDao.observeEntriesFiltered(
            mediaType = mediaType.name,
            statusFilter = statuses.isNotEmpty(),
            statuses = statuses.map { it.name },
            sortBy = sortBy.name,
            ascending = ascending
        ).map { entities -> entities.map { it.toDomain() } }
    }

    override fun searchList(query: String): Flow<List<MediaListEntry>> {
        return mediaListDao.searchEntries(query)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeStats(mediaType: MediaType): Flow<MediaListStats> {
        return observeList(mediaType, null).map { entries ->
            MediaListStats(
                totalEntries = entries.size,
                currentCount = entries.count { it.status == MediaListStatus.CURRENT },
                completedCount = entries.count { it.status == MediaListStatus.COMPLETED },
                planningCount = entries.count { it.status == MediaListStatus.PLANNING },
                droppedCount = entries.count { it.status == MediaListStatus.DROPPED },
                pausedCount = entries.count { it.status == MediaListStatus.PAUSED },
                repeatingCount = entries.count { it.status == MediaListStatus.REPEATING },
                totalProgress = entries.sumOf { it.progress },
                averageScore = entries.mapNotNull { it.score }.average().takeIf { !it.isNaN() }?.toFloat()
            )
        }
    }

    override suspend fun importFromMAL(xmlContent: String, mediaType: MediaType): Result<ImportResult> = resultOf {

        val parsedEntries = try {
            malXmlMapper.parseMALXml(xmlContent, mediaType)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

        val successful = mutableListOf<MediaListEntry>()
        val conflicts = mutableListOf<ImportConflict>()
        val errors = mutableListOf<ImportError>()

        parsedEntries.forEachIndexed { index, entity ->
            try {
                val existingEntry = mediaListDao.getEntryByMedia(entity.mediaId, entity.mediaType)

                if (existingEntry != null) {
                    val localEntry = existingEntry.toDomain()
                    val importedEntry = entity.toDomain()

                    val conflictReason = determineConflictReason(localEntry, importedEntry)

                    conflicts.add(
                        ImportConflict(
                            mediaId = entity.mediaId,
                            mediaType = MediaType.valueOf(entity.mediaType),
                            localEntry = localEntry,
                            importedEntry = importedEntry,
                            conflictReason = conflictReason
                        )
                    )
                } else {
                    val domainEntry = entity.toDomain()
                    val mediaTypeEnum = MediaType.valueOf(entity.mediaType)
                    val total = if (mediaTypeEnum == MediaType.ANIME) {
                        domainEntry.mediaInfo?.totalEpisodes
                    } else {
                        domainEntry.mediaInfo?.totalChapters
                    }

                    val validationResult = MediaListValidator.validateStatusProgressConsistency(
                        newStatus = MediaListStatus.valueOf(entity.status),
                        newProgress = domainEntry.progress,
                        total = total,
                        mediaStatus = entity.mediaStatus
                    )

                    when (validationResult) {
                        is MediaListValidator.ValidationResult.Valid -> {
                            mediaListDao.insertEntry(entity)
                            successful.add(domainEntry)
                        }
                        is MediaListValidator.ValidationResult.Invalid -> {
                            errors.add(
                                ImportError(
                                    mediaId = entity.mediaId,
                                    mediaType = mediaTypeEnum,
                                    reason = ErrorReason.VALIDATION_FAILED,
                                    message = "Validation failed: ${validationResult.error}",
                                    xmlEntryIndex = index
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                errors.add(
                    ImportError(
                        mediaId = entity.mediaId,
                        mediaType = MediaType.valueOf(entity.mediaType),
                        reason = ErrorReason.UNKNOWN,
                        message = e.message ?: "Unknown error occurred",
                        xmlEntryIndex = index
                    )
                )
            }
        }

        ImportResult(
            successful = successful,
            conflicts = conflicts,
            errors = errors,
            totalProcessed = parsedEntries.size
        )
    }

    private fun determineConflictReason(
        localEntry: MediaListEntry,
        importedEntry: MediaListEntry
    ): ConflictReason {
        return when {
            localEntry.updatedAt > importedEntry.updatedAt -> ConflictReason.NEWER_LOCAL_UPDATE
            importedEntry.updatedAt > localEntry.updatedAt -> ConflictReason.NEWER_IMPORT_UPDATE
            localEntry.status != importedEntry.status -> ConflictReason.DIFFERENT_STATUS
            localEntry.progress != importedEntry.progress -> ConflictReason.DIFFERENT_PROGRESS
            else -> ConflictReason.ALREADY_EXISTS
        }
    }

    override suspend fun exportToMAL(mediaType: MediaType): Result<String> = resultOf {
        val entries = mediaListDao.observeEntriesByType(mediaType.name).first()
        malXmlMapper.generateMALXml(entries, mediaType)
    }

    override suspend fun syncWithAniList(): Result<Unit> {
        // TODO: Future implementation
        return Result.failure(NotImplementedError("AniList sync not yet implemented"))
    }

    override suspend fun getUnsyncedEntries(): List<MediaListEntry> {
        return mediaListDao.getUnsyncedEntries().map { it.toDomain() }
    }

    override suspend fun getAllEntries(): List<MediaListEntry> {
        return mediaListDao.getAllEntries().map { it.toDomain() }
    }

    override suspend fun resolveConflict(
        conflict: ImportConflict,
        resolution: ConflictResolution
    ): Result<MediaListEntry> = resultOf {
        val entryToSave = when (resolution) {
            is ConflictResolution.KeepLocal -> {
                conflict.localEntry.copy(
                    updatedAt = System.currentTimeMillis(),
                    needsSync = true
                )
            }
            is ConflictResolution.UseImported -> {
                conflict.localEntry.copy(
                    status = conflict.importedEntry.status,
                    progress = conflict.importedEntry.progress,
                    progressVolumes = conflict.importedEntry.progressVolumes,
                    score = conflict.importedEntry.score,
                    startDate = conflict.importedEntry.startDate,
                    finishDate = conflict.importedEntry.finishDate,
                    notes = conflict.importedEntry.notes,
                    repeatCount = conflict.importedEntry.repeatCount,
                    priority = conflict.importedEntry.priority,
                    updatedAt = System.currentTimeMillis(),
                    needsSync = true
                )
            }
            is ConflictResolution.MergeFields -> {
                conflict.localEntry.copy(
                    status = if (resolution.useLocalStatus) conflict.localEntry.status else conflict.importedEntry.status,
                    progress = if (resolution.useLocalProgress) conflict.localEntry.progress else conflict.importedEntry.progress,
                    score = if (resolution.useLocalScore) conflict.localEntry.score else conflict.importedEntry.score,
                    notes = if (resolution.useLocalNotes) conflict.localEntry.notes else conflict.importedEntry.notes,
                    startDate = if (resolution.useLocalDates) conflict.localEntry.startDate else conflict.importedEntry.startDate,
                    finishDate = if (resolution.useLocalDates) conflict.localEntry.finishDate else conflict.importedEntry.finishDate,
                    updatedAt = System.currentTimeMillis(),
                    needsSync = true
                )
            }
        }

        val entity = entryToSave.toEntity()
        mediaListDao.updateEntry(entity)
        entryToSave
    }

    override suspend fun resolveAllConflicts(
        conflicts: List<ImportConflict>,
        resolution: ConflictResolution
    ): Result<List<MediaListEntry>> = resultOf {
        conflicts.map { conflict ->
            resolveConflict(conflict, resolution).getOrThrow()
        }
    }
}
