package com.yumedev.seijakulistkmp.features.auth.data.repository

import android.content.Context
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.data.helper.GoogleSignInHelper
import com.yumedev.seijakulistkmp.features.auth.data.mapper.toAuthUser
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthError
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthUser
import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val context: Context,
    private val webClientId: String
) : AuthRepository {

    private val auth = Firebase.auth
    private val googleSignInHelper = GoogleSignInHelper()

    override suspend fun signInWithEmail(email: String, password: String): Result<AuthUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password)
            val user = authResult.user ?: return Result.Failure(AuthError.UserNotFound())
            Result.Success(user.toAuthUser())
        } catch (e: FirebaseAuthException) {
            Result.Failure(mapFirebaseException(e))
        } catch (e: Exception) {
            Result.Failure(AuthError.Unknown(e.message ?: "Unknown error"))
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password)
            val user = authResult.user ?: return Result.Failure(AuthError.Unknown("Failed to create user"))
            Result.Success(user.toAuthUser())
        } catch (e: FirebaseAuthException) {
            Result.Failure(mapFirebaseException(e))
        } catch (e: Exception) {
            Result.Failure(AuthError.Unknown(e.message ?: "Unknown error"))
        }
    }

    override suspend fun signInWithGoogle(activityContext: Any?): Result<AuthUser> {
        val contextToUse = (activityContext as? Context) ?: context
        val tokenResult = googleSignInHelper.getGoogleIdToken(contextToUse, webClientId)

        return when (tokenResult) {
            is Result.Success -> {
                try {
                    val credential = GoogleAuthProvider.credential(tokenResult.data, null)
                    val authResult = auth.signInWithCredential(credential)
                    val user = authResult.user ?: return Result.Failure(AuthError.GoogleSignInFailed("Failed to get user"))
                    Result.Success(user.toAuthUser())
                } catch (e: FirebaseAuthException) {
                    Result.Failure(mapFirebaseException(e))
                } catch (e: Exception) {
                    Result.Failure(AuthError.Unknown(e.message ?: "Unknown error"))
                }
            }
            is Result.Failure -> Result.Failure(tokenResult.exception as? AuthError ?: AuthError.Unknown(tokenResult.exception.message ?: "Google sign-in failed"))
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(AuthError.Unknown(e.message ?: "Failed to sign out"))
        }
    }

    override fun getCurrentUser(): Flow<AuthUser?> {
        return auth.authStateChanged.map { firebaseUser ->
            firebaseUser?.toAuthUser()
        }
    }

    override suspend fun getCurrentUserSync(): AuthUser? {
        return auth.currentUser?.toAuthUser()
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return auth.authStateChanged.map { it != null }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email)
            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.Failure(mapFirebaseException(e))
        } catch (e: Exception) {
            Result.Failure(AuthError.Unknown(e.message ?: "Failed to send reset email"))
        }
    }

    private fun mapFirebaseException(exception: FirebaseAuthException): AuthError {
        return when (exception.errorCode) {
            "ERROR_INVALID_EMAIL" -> AuthError.InvalidEmail()
            "ERROR_WEAK_PASSWORD" -> AuthError.WeakPassword()
            "ERROR_EMAIL_ALREADY_IN_USE" -> AuthError.EmailAlreadyInUse()
            "ERROR_USER_NOT_FOUND" -> AuthError.UserNotFound()
            "ERROR_WRONG_PASSWORD" -> AuthError.WrongPassword()
            "ERROR_USER_DISABLED" -> AuthError.UserDisabled()
            "ERROR_TOO_MANY_REQUESTS" -> AuthError.TooManyRequests()
            "ERROR_NETWORK_REQUEST_FAILED" -> AuthError.NetworkError()
            else -> AuthError.Unknown(exception.message ?: "Unknown Firebase error")
        }
    }
}
