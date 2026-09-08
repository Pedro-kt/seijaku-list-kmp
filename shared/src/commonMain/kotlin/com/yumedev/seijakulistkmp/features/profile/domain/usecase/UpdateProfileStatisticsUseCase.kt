package com.yumedev.seijakulistkmp.features.profile.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.profile.domain.repository.ProfileRepository

class UpdateProfileStatisticsUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return profileRepository.updateStatistics()
    }
}
