package com.yumedev.seijakulistkmp.features.auth.domain.usecase

import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthUser
import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthUser?> {
        return authRepository.getCurrentUser()
    }
}
