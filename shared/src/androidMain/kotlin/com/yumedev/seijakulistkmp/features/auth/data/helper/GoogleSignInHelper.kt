package com.yumedev.seijakulistkmp.features.auth.data.helper

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthError

class GoogleSignInHelper {
    suspend fun getGoogleIdToken(context: Context, webClientId: String): Result<String> {
        return try {
            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )

            val credential = result.credential

            when (credential) {
                is GoogleIdTokenCredential -> {
                    Result.Success(credential.idToken)
                }
                else -> {
                    Result.Failure(AuthError.GoogleSignInFailed("Unexpected credential type"))
                }
            }
        } catch (e: GetCredentialCancellationException) {
            Result.Failure(AuthError.GoogleSignInCancelled())
        } catch (e: GetCredentialException) {
            Result.Failure(AuthError.GoogleSignInFailed(e.message ?: "Unknown error"))
        } catch (e: Exception) {
            Result.Failure(AuthError.Unknown(e.message ?: "Unknown error during Google Sign-In"))
        }
    }
}
