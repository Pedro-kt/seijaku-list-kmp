package com.yumedev.seijakulistkmp.features.auth.domain.repository

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<AuthUser>

    suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser>

    suspend fun signInWithGoogle(activityContext: Any? = null): Result<AuthUser>

    suspend fun signOut(): Result<Unit>

    fun getCurrentUser(): Flow<AuthUser?>

    suspend fun getCurrentUserSync(): AuthUser?

    fun isAuthenticated(): Flow<Boolean>

    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}
