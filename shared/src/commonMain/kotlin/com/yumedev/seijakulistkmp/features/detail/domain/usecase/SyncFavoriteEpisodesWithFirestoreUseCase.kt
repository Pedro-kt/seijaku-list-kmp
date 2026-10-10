package com.yumedev.seijakulistkmp.features.detail.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.usecase.GetCurrentUserUseCase
import com.yumedev.seijakulistkmp.features.detail.data.local.dao.FavoriteEpisodeDao
import com.yumedev.seijakulistkmp.features.detail.data.mapper.toDomain
import com.yumedev.seijakulistkmp.features.detail.data.mapper.toEntity
import com.yumedev.seijakulistkmp.features.detail.data.remote.FirestoreFavoriteEpisodeDataSource
import com.yumedev.seijakulistkmp.features.detail.data.remote.mapper.toDomain as dtoToDomain
import com.yumedev.seijakulistkmp.features.detail.data.remote.mapper.toDto
import kotlinx.coroutines.flow.first

class SyncFavoriteEpisodesWithFirestoreUseCase(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val favoriteEpisodeDao: FavoriteEpisodeDao,
    private val firestoreDataSource: FirestoreFavoriteEpisodeDataSource
) {
    suspend operator fun invoke(): Result<SyncResult> {
        val currentUser = getCurrentUser().first()
            ?: return Result.Failure(Exception("User not authenticated"))

        val uid = currentUser.uid
        val userId = currentUser.uid

        try {
            val episodesWithDifferentUserId = favoriteEpisodeDao.getAllExceptUser(uid)
            if (episodesWithDifferentUserId.isNotEmpty()) {

                val existingEpisodesForUser = favoriteEpisodeDao.getAllByUser(uid)
                val existingKeys = existingEpisodesForUser.map {
                    it.mediaId to it.episodeNumber
                }.toSet()

                var migrated = 0
                var deleted = 0
                episodesWithDifferentUserId.forEach { oldEpisode ->
                    val key = oldEpisode.mediaId to oldEpisode.episodeNumber

                    if (key in existingKeys) {
                        favoriteEpisodeDao.deleteById(oldEpisode.id)
                        deleted++
                    } else {
                        val migratedEpisode = oldEpisode.copy(
                            id = 0,
                            userId = uid
                        )
                        favoriteEpisodeDao.insert(migratedEpisode)
                        favoriteEpisodeDao.deleteById(oldEpisode.id)
                        migrated++
                    }
                }
            }
        } catch (e: Exception) {
            //TODO
        }

        val localEpisodes = favoriteEpisodeDao.getAllByUser(userId).map { it.toDomain() }

        val firestoreResult = firestoreDataSource.getAllEpisodes(uid)
        if (firestoreResult is Result.Failure) {
            return Result.Failure(firestoreResult.exception)
        }

        val firestoreEpisodes = (firestoreResult as Result.Success).data
            .map { it.dtoToDomain(id = 0L, userId = userId) }

        val localEpisodesMap = localEpisodes.associateBy { it.mediaId to it.episodeNumber }
        val firestoreEpisodesMap = firestoreEpisodes.associateBy { it.mediaId to it.episodeNumber }

        var downloaded = 0
        var uploaded = 0
        var merged = 0

        firestoreEpisodesMap.forEach { (key, cloudEpisode) ->
            val localEpisode = localEpisodesMap[key]

            when {
                localEpisode == null -> {
                    val entityToInsert = cloudEpisode.copy(id = 0L).toEntity()
                    favoriteEpisodeDao.insert(entityToInsert)
                    downloaded++
                }

                shouldUseCloudVersion(cloudEpisode, localEpisode) -> {
                    val entityToUpdate = cloudEpisode.copy(id = localEpisode.id).toEntity()
                    favoriteEpisodeDao.insert(entityToUpdate)
                    merged++
                }
            }
        }

        localEpisodesMap.forEach { (key, localEpisode) ->
            val cloudEpisode = firestoreEpisodesMap[key]

            when {
                cloudEpisode == null -> {
                    val dto = localEpisode.toDto()
                    val saveResult = firestoreDataSource.saveEpisode(uid, dto)

                    if (saveResult is Result.Success) {
                        val updatedEntity = localEpisode.copy(
                            syncedAt = System.currentTimeMillis()
                        ).toEntity()
                        favoriteEpisodeDao.insert(updatedEntity)
                        uploaded++
                    } else {
                        //TODO
                    }
                }

                shouldUseLocalVersion(localEpisode, cloudEpisode) -> {
                    val dto = localEpisode.toDto()
                    val saveResult = firestoreDataSource.saveEpisode(uid, dto)

                    if (saveResult is Result.Success) {
                        val updatedEntity = localEpisode.copy(
                            syncedAt = System.currentTimeMillis()
                        ).toEntity()
                        favoriteEpisodeDao.insert(updatedEntity)
                        merged++
                    } else {
                        //TODO
                    }
                }
            }
        }


        return Result.Success(
            SyncResult(
                downloaded = downloaded,
                uploaded = uploaded,
                merged = merged,
                total = localEpisodes.size + downloaded
            )
        )
    }

    private fun shouldUseCloudVersion(cloud: com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode, local: com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode): Boolean {
        return cloud.syncedAt != null && cloud.syncedAt > local.markedAt
    }

    private fun shouldUseLocalVersion(local: com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode, cloud: com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode): Boolean {
        if (local.syncedAt == null) return true

        val cloudSyncedAt = cloud.syncedAt ?: return true
        return local.markedAt > cloudSyncedAt
    }

    data class SyncResult(
        val downloaded: Int,
        val uploaded: Int,
        val merged: Int,
        val total: Int
    )
}
