package com.yumedev.seijakulistkmp.features.detail.data.repository

import com.yumedev.seijakulistkmp.features.detail.data.local.dao.FavoriteEpisodeDao
import com.yumedev.seijakulistkmp.features.detail.data.local.entity.FavoriteEpisodeEntity
import com.yumedev.seijakulistkmp.features.detail.data.mapper.toDomain
import com.yumedev.seijakulistkmp.features.detail.data.mapper.toEntity
import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import com.yumedev.seijakulistkmp.features.detail.domain.repository.FavoriteEpisodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class FavoriteEpisodeRepositoryImpl(
    private val favoriteEpisodeDao: FavoriteEpisodeDao
) : FavoriteEpisodeRepository {

    override suspend fun toggleFavorite(
        userId: String,
        mediaId: Int,
        episodeNumber: Int,
        episodeTitle: String,
        thumbnailUrl: String?
    ): Result<Unit> {
        return try {
            val exists = favoriteEpisodeDao.isFavorite(userId, mediaId, episodeNumber)

            if (exists) {
                favoriteEpisodeDao.deleteByEpisode(userId, mediaId, episodeNumber)
            } else {
                val favoriteEpisode = FavoriteEpisodeEntity(
                    userId = userId,
                    mediaId = mediaId,
                    episodeNumber = episodeNumber,
                    episodeTitle = episodeTitle,
                    thumbnailUrl = thumbnailUrl,
                    markedAt = Clock.System.now().toEpochMilliseconds()
                )
                favoriteEpisodeDao.insert(favoriteEpisode)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFavorite(favoriteEpisode: FavoriteEpisode): Result<Unit> {
        return try {
            favoriteEpisodeDao.insert(favoriteEpisode.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFavorite(
        userId: String,
        mediaId: Int,
        episodeNumber: Int
    ): Result<Unit> {
        return try {
            favoriteEpisodeDao.deleteByEpisode(userId, mediaId, episodeNumber)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeFavoritesByMedia(
        userId: String,
        mediaId: Int
    ): Flow<List<FavoriteEpisode>> {
        return favoriteEpisodeDao.observeFavoritesByMedia(userId, mediaId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getFavoritesByMedia(
        userId: String,
        mediaId: Int
    ): Result<List<FavoriteEpisode>> {
        return try {
            val favorites = favoriteEpisodeDao.getFavoritesByMedia(userId, mediaId)
                .map { it.toDomain() }
            Result.success(favorites)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllFavorites(userId: String): Result<List<FavoriteEpisode>> {
        return try {
            val favorites = favoriteEpisodeDao.getAllByUser(userId)
                .map { it.toDomain() }
            Result.success(favorites)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isFavorite(
        userId: String,
        mediaId: Int,
        episodeNumber: Int
    ): Boolean {
        return favoriteEpisodeDao.isFavorite(userId, mediaId, episodeNumber)
    }

    override fun observeIsFavorite(
        userId: String,
        mediaId: Int,
        episodeNumber: Int
    ): Flow<Boolean> {
        return favoriteEpisodeDao.observeIsFavorite(userId, mediaId, episodeNumber)
    }
}
