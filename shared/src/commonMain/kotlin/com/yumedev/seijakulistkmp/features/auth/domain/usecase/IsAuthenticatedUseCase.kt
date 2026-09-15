package com.yumedev.seijakulistkmp.features.auth.domain.usecase

import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class IsAuthenticatedUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return authRepository.isAuthenticated()
    }
}
