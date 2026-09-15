package com.yumedev.seijakulistkmp.features.auth.domain.model

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val isEmailVerified: Boolean,
    val isAnonymous: Boolean,
    val providers: List<AuthProvider>,
)

enum class AuthProvider {
    EMAIL_PASSWORD,
    GOOGLE,
    ANILIST,
    ANONYMOUS
}
