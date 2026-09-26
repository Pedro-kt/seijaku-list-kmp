package com.yumedev.seijakulistkmp.features.detail.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.yumedev.seijakulistkmp.features.detail.data.local.entity.FavoriteEpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteEpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEpisode: FavoriteEpisodeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favoriteEpisodes: List<FavoriteEpisodeEntity>)

    @Delete
    suspend fun delete(favoriteEpisode: FavoriteEpisodeEntity)

    @Query("DELETE FROM favorite_episodes WHERE user_id = :userId AND media_id = :mediaId AND episode_number = :episodeNumber")
    suspend fun deleteByEpisode(userId: String, mediaId: Int, episodeNumber: Int)

    @Query("SELECT * FROM favorite_episodes WHERE user_id = :userId AND media_id = :mediaId")
    fun observeFavoritesByMedia(userId: String, mediaId: Int): Flow<List<FavoriteEpisodeEntity>>

    @Query("SELECT * FROM favorite_episodes WHERE user_id = :userId AND media_id = :mediaId")
    suspend fun getFavoritesByMedia(userId: String, mediaId: Int): List<FavoriteEpisodeEntity>

    @Query("SELECT * FROM favorite_episodes WHERE user_id = :userId")
    suspend fun getAllByUser(userId: String): List<FavoriteEpisodeEntity>

    @Query("SELECT * FROM favorite_episodes WHERE user_id = :userId")
    fun observeAllByUser(userId: String): Flow<List<FavoriteEpisodeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_episodes WHERE user_id = :userId AND media_id = :mediaId AND episode_number = :episodeNumber LIMIT 1)")
    suspend fun isFavorite(userId: String, mediaId: Int, episodeNumber: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_episodes WHERE user_id = :userId AND media_id = :mediaId AND episode_number = :episodeNumber LIMIT 1)")
    fun observeIsFavorite(userId: String, mediaId: Int, episodeNumber: Int): Flow<Boolean>

    @Query("DELETE FROM favorite_episodes WHERE user_id = :userId")
    suspend fun deleteAllByUser(userId: String)
}
