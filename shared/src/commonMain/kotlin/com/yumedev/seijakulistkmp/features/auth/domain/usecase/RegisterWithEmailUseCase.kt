package com.yumedev.seijakulistkmp.features.auth.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthError
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthUser
import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository

class RegisterWithEmailUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, confirmPassword: String): Result<AuthUser> {
        if (email.isBlank()) {
            return Result.Failure(AuthError.InvalidEmail("Email cannot be empty"))
        }

        if (!isValidEmail(email)) {
            return Result.Failure(AuthError.InvalidEmail())
        }

        if (password.isBlank()) {
            return Result.Failure(AuthError.WeakPassword("Password cannot be empty"))
        }

        val passwordValidation = validatePasswordStrength(password)
        if (passwordValidation != null) {
            return Result.Failure(AuthError.WeakPassword(passwordValidation))
        }

        if (password != confirmPassword) {
            return Result.Failure(AuthError.WeakPassword("Passwords do not match"))
        }

        return authRepository.signUpWithEmail(email.trim(), password)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    private fun validatePasswordStrength(password: String): String? {
        return when {
            password.length < 12 -> "Password must be at least 12 characters long"
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
            !password.any { it.isDigit() } -> "Password must contain at least one number"
            !password.any { !it.isLetterOrDigit() } -> "Password must contain at least one special character"
            else -> null
        }
    }
}
