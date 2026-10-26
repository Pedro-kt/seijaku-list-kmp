package com.yumedev.seijakulistkmp.features.profile.data.repository

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.core.domain.model.resultOf
import com.yumedev.seijakulistkmp.core.utils.ImageReader
import com.yumedev.seijakulistkmp.features.profile.data.local.dao.UserProfileDao
import com.yumedev.seijakulistkmp.features.profile.data.local.entity.UserProfileEntity
import com.yumedev.seijakulistkmp.features.profile.data.remote.FirebaseStorageDataSource
import com.yumedev.seijakulistkmp.features.profile.data.remote.FirestoreProfileDataSource
import com.yumedev.seijakulistkmp.features.profile.data.remote.dto.ProfileImageDto
import com.yumedev.seijakulistkmp.features.profile.domain.model.ProfileImage
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.profile.domain.repository.ProfileRepository
import com.yumedev.seijakulistkmp.features.tracking.data.local.dao.MediaListDao
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class ProfileRepositoryImpl(
    private val userProfileDao: UserProfileDao,
    private val mediaListDao: MediaListDao,
    private val firebaseStorageDataSource: FirebaseStorageDataSource,
    private val firestoreProfileDataSource: FirestoreProfileDataSource,
    private val imageReader: ImageReader,
) : ProfileRepository {

    override fun getCurrentProfile(): Flow<UserProfile?> {
        return userProfileDao.getCurrentProfile().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getCurrentProfileSync(): UserProfile? {
        return userProfileDao.getCurrentProfileSync()?.toDomain()
    }

    override suspend fun updateProfile(profile: UserProfile): Result<Unit> = resultOf {
        val entity = UserProfileEntity.fromDomain(profile)
        userProfileDao.updateProfile(entity)
    }

    override suspend fun createOrUpdateLocalProfile(
        name: String,
        about: String?,
        avatarUrl: String?,
        bannerUrl: String?,
    ): Result<UserProfile> = resultOf {
        val existing = userProfileDao.getCurrentProfileSync()
        val now = Clock.System.now().toEpochMilliseconds()

        val profile = if (existing != null) {
            existing.copy(
                name = name,
                about = about,
                avatarLarge = avatarUrl,
                avatarMedium = avatarUrl,
                banner = bannerUrl,
                updatedAt = now,
            )
        } else {
            UserProfileEntity(
                id = 1,
                anilistId = null,
                name = name,
                about = about,
                avatarLarge = avatarUrl,
                avatarMedium = avatarUrl,
                banner = bannerUrl,
                isLocal = true,
                createdAt = now,
                updatedAt = now,
            )
        }

        userProfileDao.upsertProfile(profile)
        profile.toDomain()
    }

    override suspend fun syncWithAnilist(): Result<UserProfile> {
        // TODO: Implement when Anilist authentication is ready
        return Result.failure(NotImplementedError("Anilist sync not yet implemented"))
    }

    override suspend fun updateStatistics(): Result<Unit> = resultOf {
        val profile = userProfileDao.getCurrentProfileSync()
            ?: throw IllegalStateException("No profile exists")

        val allEntries = mediaListDao.getAllEntries()
        val animeEntries = allEntries.filter { it.mediaType == "ANIME" }
        val mangaEntries = allEntries.filter { it.mediaType == "MANGA" }

        val animeCount = animeEntries.size
        val animeMeanScore = if (animeEntries.isNotEmpty()) {
            animeEntries.mapNotNull { it.score?.toDouble() }.average()
        } else {
            0.0
        }
        val episodesWatched = animeEntries.sumOf { it.progress }
        val minutesWatched = 0 // TODO: Calculate based on episode duration when available

        val mangaCount = mangaEntries.size
        val mangaMeanScore = if (mangaEntries.isNotEmpty()) {
            mangaEntries.mapNotNull { it.score?.toDouble() }.average()
        } else {
            0.0
        }
        val chaptersRead = mangaEntries.sumOf { it.progress }
        val volumesRead = mangaEntries.sumOf { it.progressVolumes ?: 0 }

        userProfileDao.updateStatistics(
            profileId = profile.id,
            animeCount = animeCount,
            animeMeanScore = animeMeanScore,
            minutesWatched = minutesWatched,
            episodesWatched = episodesWatched,
            mangaCount = mangaCount,
            mangaMeanScore = mangaMeanScore,
            chaptersRead = chaptersRead,
            volumesRead = volumesRead,
            updatedAt = Clock.System.now().toEpochMilliseconds(),
        )
    }

    override suspend fun hasLocalProfile(): Boolean {
        return userProfileDao.hasLocalProfile()
    }

    override suspend fun ensureLocalProfileExists(): Result<UserProfile> = resultOf {
        if (!hasLocalProfile()) {
            val defaultProfile = UserProfileEntity.createDefault()
            userProfileDao.upsertProfile(defaultProfile)
            defaultProfile.toDomain()
        } else {
            userProfileDao.getCurrentProfileSync()?.toDomain()
                ?: throw IllegalStateException("Profile should exist but was not found")
        }
    }

    override suspend fun uploadAndUpdateAvatar(imageUri: String): Result<UserProfile> = resultOf {
        val userId = Firebase.auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val imageBytes = imageReader.readImageBytes(imageUri)

        val uploadResult = firebaseStorageDataSource.uploadAvatar(userId, imageBytes)
        val avatarUrl = when (uploadResult) {
            is Result.Success<String> -> uploadResult.data
            is Result.Failure -> throw uploadResult.exception
        }

        val currentProfile = userProfileDao.getCurrentProfileSync()
            ?: throw IllegalStateException("No profile exists")

        val oldAvatarUrl = currentProfile.avatarLarge
        if (oldAvatarUrl != null && oldAvatarUrl.contains("firebase")) {
            firebaseStorageDataSource.deleteImage(oldAvatarUrl)
        }

        val now = Clock.System.now().toEpochMilliseconds()
        val updatedProfile = currentProfile.copy(
            avatarLarge = avatarUrl,
            avatarMedium = avatarUrl,
            updatedAt = now,
        )

        userProfileDao.upsertProfile(updatedProfile)

        val updates = mapOf(
            "avatar" to ProfileImageDto(
                large = avatarUrl,
                medium = avatarUrl
            ),
            "updatedAt" to now
        )

        when (val firestoreResult = firestoreProfileDataSource.updateProfile(userId, updates)) {
            is Result.Failure -> throw firestoreResult.exception
            else -> {}
        }

        updatedProfile.toDomain()
    }

    override suspend fun uploadAndUpdateBanner(imageUri: String): Result<UserProfile> = resultOf {
        val userId = Firebase.auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val imageBytes = imageReader.readImageBytes(imageUri)

        val uploadResult = firebaseStorageDataSource.uploadBanner(userId, imageBytes)
        val bannerUrl = when (uploadResult) {
            is Result.Success<String> -> uploadResult.data
            is Result.Failure -> throw uploadResult.exception
        }

        val currentProfile = userProfileDao.getCurrentProfileSync()
            ?: throw IllegalStateException("No profile exists")

        val oldBannerUrl = currentProfile.banner
        if (oldBannerUrl != null && oldBannerUrl.contains("firebase")) {
            firebaseStorageDataSource.deleteImage(oldBannerUrl)
        }

        val now = Clock.System.now().toEpochMilliseconds()
        val updatedProfile = currentProfile.copy(
            banner = bannerUrl,
            updatedAt = now,
        )

        userProfileDao.upsertProfile(updatedProfile)

        val updates = mapOf(
            "banner" to bannerUrl,
            "updatedAt" to now
        )

        when (val firestoreResult = firestoreProfileDataSource.updateProfile(userId, updates)) {
            is Result.Failure -> throw firestoreResult.exception
            else -> {}
        }

        updatedProfile.toDomain()
    }

    override suspend fun removeAvatar(): Result<UserProfile> = resultOf {
        val userId = Firebase.auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val currentProfile = userProfileDao.getCurrentProfileSync()
            ?: throw IllegalStateException("No profile exists")

        val oldAvatarUrl = currentProfile.avatarLarge
        if (oldAvatarUrl != null && oldAvatarUrl.contains("firebase")) {
            firebaseStorageDataSource.deleteImage(oldAvatarUrl)
        }

        val now = Clock.System.now().toEpochMilliseconds()
        val updatedProfile = currentProfile.copy(
            avatarLarge = null,
            avatarMedium = null,
            updatedAt = now,
        )

        userProfileDao.upsertProfile(updatedProfile)

        val updates = mapOf<String, Any?>(
            "avatar" to null,
            "updatedAt" to now
        )

        when (val firestoreResult = firestoreProfileDataSource.updateProfile(userId, updates)) {
            is Result.Failure -> throw firestoreResult.exception
            else -> {}
        }

        updatedProfile.toDomain()
    }

    override suspend fun removeBanner(): Result<UserProfile> = resultOf {
        val userId = Firebase.auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val currentProfile = userProfileDao.getCurrentProfileSync()
            ?: throw IllegalStateException("No profile exists")

        val oldBannerUrl = currentProfile.banner
        if (oldBannerUrl != null && oldBannerUrl.contains("firebase")) {
            firebaseStorageDataSource.deleteImage(oldBannerUrl)
        }

        val now = Clock.System.now().toEpochMilliseconds()
        val updatedProfile = currentProfile.copy(
            banner = null,
            updatedAt = now,
        )

        userProfileDao.upsertProfile(updatedProfile)

        val updates = mapOf<String, Any?>(
            "banner" to null,
            "updatedAt" to now
        )

        when (val firestoreResult = firestoreProfileDataSource.updateProfile(userId, updates)) {
            is Result.Failure -> throw firestoreResult.exception
            else -> {}
        }

        updatedProfile.toDomain()
    }
}
