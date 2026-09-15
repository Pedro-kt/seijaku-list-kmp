package com.yumedev.seijakulistkmp.features.auth.data.mapper

import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthProvider
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthUser
import dev.gitlive.firebase.auth.FirebaseUser

fun FirebaseUser.toAuthUser(): AuthUser {
    val providers = this.providerData.mapNotNull { providerInfo ->
        when (providerInfo.providerId) {
            "password" -> AuthProvider.EMAIL_PASSWORD
            "google.com" -> AuthProvider.GOOGLE
            else -> null
        }
    }.distinct()

    return AuthUser(
        uid = this.uid,
        email = this.email,
        displayName = this.displayName,
        photoUrl = null,
        isEmailVerified = this.isEmailVerified,
        isAnonymous = this.isAnonymous,
        providers = providers.ifEmpty { listOf(AuthProvider.ANONYMOUS) }
    )
}
