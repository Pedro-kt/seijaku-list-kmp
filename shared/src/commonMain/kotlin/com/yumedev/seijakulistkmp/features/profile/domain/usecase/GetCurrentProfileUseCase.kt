package com.yumedev.seijakulistkmp.features.profile.domain.usecase

import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<UserProfile?> {
        return profileRepository.getCurrentProfile()
    }
}
