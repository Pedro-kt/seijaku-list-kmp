package com.yumedev.seijakulistkmp.features.profile.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository
import com.yumedev.seijakulistkmp.features.profile.data.remote.FirestoreProfileDataSource
import com.yumedev.seijakulistkmp.features.profile.data.remote.mapper.toDomain
import com.yumedev.seijakulistkmp.features.profile.data.remote.mapper.toDto
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.profile.domain.repository.ProfileRepository
import kotlinx.datetime.Clock

class SyncProfileWithFirestoreUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val firestoreDataSource: FirestoreProfileDataSource,
) {
    suspend operator fun invoke(): Result<UserProfile> {
        val currentUser = authRepository.getCurrentUserSync()
            ?: return Result.Failure(Exception("User not authenticated"))

        val uid = currentUser.uid
        val localProfile = profileRepository.getCurrentProfileSync()
        val firestoreResult = firestoreDataSource.getProfile(uid)

        if (firestoreResult is Result.Failure) {
            return Result.Failure(firestoreResult.exception)
        }

        val firestoreProfile = (firestoreResult as Result.Success).data

        return when {
            localProfile != null && firestoreProfile != null -> {
                if (firestoreProfile.updatedAt > localProfile.updatedAt.toEpochMilliseconds()) {
                    val updatedLocal = firestoreProfile.toDomain(localProfile.id)
                    val updateResult = profileRepository.updateProfile(updatedLocal)
                    when (updateResult) {
                        is Result.Success -> Result.Success(updatedLocal)
                        is Result.Failure -> Result.Failure(updateResult.exception)
                    }
                } else {
                    val avatarToUse = if (localProfile.avatar?.large == null && firestoreProfile.avatar?.large != null) {
                        firestoreProfile.avatar.large
                    } else {
                        localProfile.avatar?.large
                    }

                    val bannerToUse = if (localProfile.banner == null && firestoreProfile.banner != null) {
                        firestoreProfile.banner
                    } else {
                        localProfile.banner
                    }

                    val profileToSync = if (avatarToUse != localProfile.avatar?.large || bannerToUse != localProfile.banner) {
                        val updateResult = profileRepository.createOrUpdateLocalProfile(
                            name = localProfile.name,
                            about = localProfile.about,
                            avatarUrl = avatarToUse,
                            bannerUrl = bannerToUse
                        )
                        when (updateResult) {
                            is Result.Success -> updateResult.data
                            is Result.Failure -> localProfile
                        }
                    } else {
                        localProfile
                    }

                    val dto = profileToSync.toDto(uid)
                    val saveResult = firestoreDataSource.saveProfile(dto)
                    when (saveResult) {
                        is Result.Success -> Result.Success(profileToSync)
                        is Result.Failure -> Result.Failure(saveResult.exception)
                    }
                }
            }

            localProfile == null && firestoreProfile != null -> {
                val newLocal = profileRepository.createOrUpdateLocalProfile(
                    name = firestoreProfile.name,
                    about = firestoreProfile.about,
                    avatarUrl = firestoreProfile.avatar?.large,
                    bannerUrl = firestoreProfile.banner
                )

                when (newLocal) {
                    is Result.Success -> Result.Success(newLocal.data)
                    is Result.Failure -> Result.Failure(newLocal.exception)
                }
            }

            localProfile != null && firestoreProfile == null -> {
                val dto = localProfile.toDto(uid)
                val saveResult = firestoreDataSource.saveProfile(dto)

                when (saveResult) {
                    is Result.Success -> Result.Success(localProfile)
                    is Result.Failure -> Result.Failure(saveResult.exception)
                }
            }

            else -> {
                val newProfile = profileRepository.createOrUpdateLocalProfile(
                    name = currentUser.displayName ?: currentUser.email ?: "User",
                    avatarUrl = currentUser.photoUrl
                )

                when (newProfile) {
                    is Result.Success -> {
                        val dto = newProfile.data.toDto(uid)
                        val saveResult = firestoreDataSource.saveProfile(dto)
                        when (saveResult) {
                            is Result.Success -> Result.Success(newProfile.data)
                            is Result.Failure -> Result.Failure(saveResult.exception)
                        }
                    }
                    is Result.Failure -> Result.Failure(newProfile.exception)
                }
            }
        }
    }
}
