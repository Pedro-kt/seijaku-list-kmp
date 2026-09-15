package com.yumedev.seijakulistkmp.features.auth.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthError
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthUser
import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository

class LoginWithEmailUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthUser> {
        if (email.isBlank()) {
            return Result.Failure(AuthError.InvalidEmail("Email cannot be empty"))
        }

        if (!isValidEmail(email)) {
            return Result.Failure(AuthError.InvalidEmail())
        }

        if (password.isBlank()) {
            return Result.Failure(AuthError.WeakPassword("Password cannot be empty"))
        }

        if (password.length < 6) {
            return Result.Failure(AuthError.WeakPassword())
        }

        return authRepository.signInWithEmail(email.trim(), password)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}
