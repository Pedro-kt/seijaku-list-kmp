package com.yumedev.seijakulistkmp.features.profile.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.profile.domain.repository.ProfileRepository

class EnsureLocalProfileExistsUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return profileRepository.ensureLocalProfileExists()
    }
}
