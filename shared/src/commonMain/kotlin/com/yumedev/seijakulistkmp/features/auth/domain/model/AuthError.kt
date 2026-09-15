package com.yumedev.seijakulistkmp.features.auth.domain.model

sealed class AuthError(override val message: String) : Throwable(message) {
    data class InvalidEmail(override val message: String = "Invalid email format") : AuthError(message)
    data class WeakPassword(override val message: String = "Password is too weak (minimum 6 characters)") : AuthError(message)
    data class EmailAlreadyInUse(override val message: String = "This email is already registered") : AuthError(message)
    data class UserNotFound(override val message: String = "No user found with this email") : AuthError(message)
    data class WrongPassword(override val message: String = "Incorrect password") : AuthError(message)
    data class UserDisabled(override val message: String = "This account has been disabled") : AuthError(message)

    data class GoogleSignInCancelled(override val message: String = "Google Sign-In was cancelled") : AuthError(message)
    data class GoogleSignInFailed(override val message: String = "Google Sign-In failed") : AuthError(message)

    data class NetworkError(override val message: String = "Network error. Check your connection") : AuthError(message)
    data class Timeout(override val message: String = "Request timed out") : AuthError(message)

    data class Unknown(override val message: String = "An unknown error occurred") : AuthError(message)
    data class TooManyRequests(override val message: String = "Too many attempts. Please try again later") : AuthError(message)
}
