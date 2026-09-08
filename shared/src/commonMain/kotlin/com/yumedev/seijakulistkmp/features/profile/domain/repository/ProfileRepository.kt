package com.yumedev.seijakulistkmp.features.profile.domain.repository

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getCurrentProfile(): Flow<UserProfile?>

    suspend fun getCurrentProfileSync(): UserProfile?

    suspend fun updateProfile(profile: UserProfile): Result<Unit>

    suspend fun createOrUpdateLocalProfile(
        name: String,
        about: String? = null,
        avatarUrl: String? = null,
        bannerUrl: String? = null,
    ): Result<UserProfile>

    suspend fun syncWithAnilist(): Result<UserProfile>

    suspend fun updateStatistics(): Result<Unit>

    suspend fun hasLocalProfile(): Boolean

    suspend fun ensureLocalProfileExists(): Result<UserProfile>
}
