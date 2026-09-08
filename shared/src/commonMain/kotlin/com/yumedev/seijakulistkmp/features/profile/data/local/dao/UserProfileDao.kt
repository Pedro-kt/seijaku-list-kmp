package com.yumedev.seijakulistkmp.features.profile.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.yumedev.seijakulistkmp.features.profile.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    fun getCurrentProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    suspend fun getCurrentProfileSync(): UserProfileEntity?

    @Query("SELECT * FROM user_profiles WHERE anilistId = :anilistId LIMIT 1")
    suspend fun getProfileByAnilistId(anilistId: Int): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: UserProfileEntity)

    @Update
    suspend fun updateProfile(profile: UserProfileEntity)

    @Query("DELETE FROM user_profiles")
    suspend fun deleteAllProfiles()

    @Query("SELECT EXISTS(SELECT 1 FROM user_profiles WHERE id = 1)")
    suspend fun hasLocalProfile(): Boolean

    @Query("""
        UPDATE user_profiles
        SET animeCount = :animeCount,
            animeMeanScore = :animeMeanScore,
            minutesWatched = :minutesWatched,
            episodesWatched = :episodesWatched,
            mangaCount = :mangaCount,
            mangaMeanScore = :mangaMeanScore,
            chaptersRead = :chaptersRead,
            volumesRead = :volumesRead,
            updatedAt = :updatedAt
        WHERE id = :profileId
    """)
    suspend fun updateStatistics(
        profileId: Int,
        animeCount: Int,
        animeMeanScore: Double,
        minutesWatched: Int,
        episodesWatched: Int,
        mangaCount: Int,
        mangaMeanScore: Double,
        chaptersRead: Int,
        volumesRead: Int,
        updatedAt: Long
    )
}
