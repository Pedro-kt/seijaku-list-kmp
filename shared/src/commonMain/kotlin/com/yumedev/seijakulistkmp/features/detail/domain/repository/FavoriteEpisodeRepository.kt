package com.yumedev.seijakulistkmp.features.detail.domain.repository

import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import kotlinx.coroutines.flow.Flow

interface FavoriteEpisodeRepository {

    suspend fun toggleFavorite(
        userId: String,
        mediaId: Int,
        episodeNumber: Int,
        episodeTitle: String,
        thumbnailUrl: String?
    ): Result<Unit>

    suspend fun addFavorite(favoriteEpisode: FavoriteEpisode): Result<Unit>

    suspend fun removeFavorite(userId: String, mediaId: Int, episodeNumber: Int): Result<Unit>

    fun observeFavoritesByMedia(userId: String, mediaId: Int): Flow<List<FavoriteEpisode>>

    suspend fun getFavoritesByMedia(userId: String, mediaId: Int): Result<List<FavoriteEpisode>>

    suspend fun getAllFavorites(userId: String): Result<List<FavoriteEpisode>>

    suspend fun isFavorite(userId: String, mediaId: Int, episodeNumber: Int): Boolean

    fun observeIsFavorite(userId: String, mediaId: Int, episodeNumber: Int): Flow<Boolean>
}
