package com.yumedev.seijakulistkmp.features.auth.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthError
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthProvider
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthUser
import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginWithEmailUseCaseTest {

    private class FakeAuthRepository : AuthRepository {
        var signInCalled = false
        var lastEmail: String? = null
        var lastPassword: String? = null

        override suspend fun signInWithEmail(email: String, password: String): Result<AuthUser> {
            signInCalled = true
            lastEmail = email
            lastPassword = password
            return Result.Success(
                AuthUser(
                    uid = "test-uid",
                    email = email,
                    displayName = "Test User",
                    photoUrl = null,
                    isEmailVerified = true,
                    isAnonymous = false,
                    provider = AuthProvider.EMAIL_PASSWORD
                )
            )
        }

        override suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser> =
            Result.Failure(AuthError.Unknown())

        override suspend fun signInWithGoogle(activityContext: Any?): Result<AuthUser> =
            Result.Failure(AuthError.Unknown())

        override suspend fun signOut(): Result<Unit> = Result.Success(Unit)

        override fun getCurrentUser() = kotlinx.coroutines.flow.flowOf<AuthUser?>(null)

        override suspend fun getCurrentUserSync(): AuthUser? = null

        override fun isAuthenticated() = kotlinx.coroutines.flow.flowOf(false)

        override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
            Result.Success(Unit)

        override suspend fun sendEmailVerification(): Result<Unit> = Result.Success(Unit)
    }

    @Test
    fun `login with empty email returns InvalidEmail error`() = runTest {
        val repository = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(repository)

        val result = useCase("", "password123")

        assertTrue(result is Result.Failure)
        assertTrue(result.exception is AuthError.InvalidEmail)
        assertEquals("Email cannot be empty", result.exception.message)
    }

    @Test
    fun `login with invalid email format returns InvalidEmail error`() = runTest {
        val repository = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(repository)

        val result = useCase("notanemail", "password123")

        assertTrue(result is Result.Failure)
        assertTrue(result.exception is AuthError.InvalidEmail)
    }

    @Test
    fun `login with blank password returns WeakPassword error`() = runTest {
        val repository = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(repository)

        val result = useCase("test@example.com", "")

        assertTrue(result is Result.Failure)
        assertTrue(result.exception is AuthError.WeakPassword)
        assertEquals("Password cannot be empty", result.exception.message)
    }

    @Test
    fun `login with valid credentials succeeds`() = runTest {
        val repository = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(repository)

        val email = "test@example.com"
        val password = "password123"
        val result = useCase(email, password)

        assertTrue(result is Result.Success)
        assertEquals("test-uid", result.data.uid)
        assertEquals(email, result.data.email)
        assertEquals("Test User", result.data.displayName)
        assertTrue(result.data.isEmailVerified)
        assertTrue(repository.signInCalled)
        assertEquals(email, repository.lastEmail)
        assertEquals(password, repository.lastPassword)
    }

    @Test
    fun `login trims email before sending to repository`() = runTest {
        val repository = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(repository)

        val email = "  test@example.com  "
        val result = useCase(email, "password123")

        assertTrue(result is Result.Success)
        assertEquals("test@example.com", repository.lastEmail)
    }
}
