package com.yumedev.seijakulistkmp.features.auth.presentation

import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthError
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthUser

data class AuthState(
    val isLoading: Boolean = false,
    val isGoogleSignInLoading: Boolean = false,

    val currentUser: AuthUser? = null,
    val isAuthenticated: Boolean = false,

    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,

    val error: AuthError? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false,
    val passwordResetEmailSent: Boolean = false,
) {
    val isLoginFormValid: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && emailError == null && passwordError == null

    val isRegisterFormValid: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null
}
