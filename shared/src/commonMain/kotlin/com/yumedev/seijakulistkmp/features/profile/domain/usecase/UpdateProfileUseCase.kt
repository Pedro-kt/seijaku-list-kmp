package com.yumedev.seijakulistkmp.features.profile.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.profile.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        name: String,
        about: String? = null,
        avatarUrl: String? = null,
        bannerUrl: String? = null,
    ): Result<UserProfile> {
        return profileRepository.createOrUpdateLocalProfile(
            name = name,
            about = about,
            avatarUrl = avatarUrl,
            bannerUrl = bannerUrl,
        )
    }
}
