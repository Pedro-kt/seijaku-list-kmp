package com.yumedev.seijakulistkmp.features.tracking.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository
import com.yumedev.seijakulistkmp.features.tracking.data.local.dao.MediaListDao
import com.yumedev.seijakulistkmp.features.tracking.data.mapper.toDomain
import com.yumedev.seijakulistkmp.features.tracking.data.mapper.toEntity
import com.yumedev.seijakulistkmp.features.tracking.data.remote.FirestoreMediaListDataSource
import com.yumedev.seijakulistkmp.features.tracking.data.remote.mapper.toDomain as dtoToDomain
import com.yumedev.seijakulistkmp.features.tracking.data.remote.mapper.toDto
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry

class SyncMediaListWithFirestoreUseCase(
    private val authRepository: AuthRepository,
    private val mediaListDao: MediaListDao,
    private val firestoreDataSource: FirestoreMediaListDataSource,
) {
    suspend operator fun invoke(): Result<SyncResult> {
        val currentUser = authRepository.getCurrentUserSync()
            ?: return Result.Failure(Exception("User not authenticated"))

        val uid = currentUser.uid
        println("SyncMediaListWithFirestoreUseCase: Starting sync for user $uid")

        val localEntries = mediaListDao.getAllEntries().map { it.toDomain() }
        println("SyncMediaListWithFirestoreUseCase: Found ${localEntries.size} local entries")

        val firestoreResult = firestoreDataSource.getAllEntries(uid)
        if (firestoreResult is Result.Failure) {
            return Result.Failure(firestoreResult.exception)
        }

        val firestoreEntries = (firestoreResult as Result.Success).data
            .map { it.dtoToDomain(0L) }

        val localEntriesMap = localEntries.associateBy { it.mediaId to it.mediaType }
        val firestoreEntriesMap = firestoreEntries.associateBy { it.mediaId to it.mediaType }

        var downloaded = 0
        var uploaded = 0
        var merged = 0

        firestoreEntriesMap.forEach { (key, cloudEntry) ->
            val localEntry = localEntriesMap[key]

            when {
                localEntry == null -> {
                    val entityToInsert = cloudEntry.copy(
                        id = 0L,
                        isSynced = true,
                        needsSync = false
                    ).toEntity()
                    mediaListDao.insertEntry(entityToInsert)
                    downloaded++
                }

                cloudEntry.updatedAt > localEntry.updatedAt -> {
                    val entityToUpdate = cloudEntry.copy(
                        id = localEntry.id,
                        isSynced = true,
                        needsSync = false
                    ).toEntity()
                    mediaListDao.updateEntry(entityToUpdate)
                    merged++
                }
            }
        }

        localEntriesMap.forEach { (key, localEntry) ->
            val cloudEntry = firestoreEntriesMap[key]

            when {
                cloudEntry == null -> {
                    val firestoreId = "${localEntry.mediaId}_${localEntry.mediaType.name}"
                    val dto = localEntry.toDto(firestoreId)
                    val saveResult = firestoreDataSource.saveEntry(uid, dto)

                    if (saveResult is Result.Success) {
                        val updatedEntity = localEntry.copy(
                            isSynced = true,
                            needsSync = false
                        ).toEntity()
                        mediaListDao.updateEntry(updatedEntity)
                        uploaded++
                    } else {
                        println("Error uploading entry ${localEntry.mediaId}: ${(saveResult as Result.Failure).exception.message}")
                    }
                }

                localEntry.updatedAt > cloudEntry.updatedAt -> {
                    val firestoreId = "${localEntry.mediaId}_${localEntry.mediaType.name}"
                    val dto = localEntry.toDto(firestoreId)
                    val saveResult = firestoreDataSource.saveEntry(uid, dto)

                    if (saveResult is Result.Success) {
                        val updatedEntity = localEntry.copy(
                            isSynced = true,
                            needsSync = false
                        ).toEntity()
                        mediaListDao.updateEntry(updatedEntity)
                        merged++
                    } else {
                        println("Error updating entry ${localEntry.mediaId}: ${(saveResult as Result.Failure).exception.message}")
                    }
                }
            }
        }

        return Result.Success(
            SyncResult(
                downloaded = downloaded,
                uploaded = uploaded,
                merged = merged,
                total = localEntries.size + downloaded
            )
        )
    }

    data class SyncResult(
        val downloaded: Int,
        val uploaded: Int,
        val merged: Int,
        val total: Int
    )
}
