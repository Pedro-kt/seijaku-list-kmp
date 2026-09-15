package com.yumedev.seijakulistkmp.features.profile.data.remote

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.profile.data.remote.dto.UserProfileDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FirestoreProfileDataSource {

    private val firestore = Firebase.firestore
    private val usersCollection = firestore.collection("users")

    suspend fun getProfile(uid: String): Result<UserProfileDto?> {
        return try {
            val document = usersCollection.document(uid).get()
            val profile = if (document.exists) {
                document.data<UserProfileDto>()
            } else {
                null
            }
            Result.Success(profile)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to get profile from Firestore: ${e.message}"))
        }
    }

    fun observeProfile(uid: String): Flow<UserProfileDto?> {
        return usersCollection.document(uid)
            .snapshots
            .map { snapshot ->
                if (snapshot.exists) {
                    snapshot.data<UserProfileDto>()
                } else {
                    null
                }
            }
            .catch { e ->
                println("Error observing profile: ${e.message}")
                emit(null)
            }
    }

    suspend fun saveProfile(profile: UserProfileDto): Result<Unit> {
        return try {
            usersCollection.document(profile.uid).set(profile)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to save profile to Firestore: ${e.message}"))
        }
    }

    suspend fun updateProfile(uid: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            usersCollection.document(uid).update(updates)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to update profile in Firestore: ${e.message}"))
        }
    }

    suspend fun deleteProfile(uid: String): Result<Unit> {
        return try {
            usersCollection.document(uid).delete()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to delete profile from Firestore: ${e.message}"))
        }
    }

    suspend fun profileExists(uid: String): Result<Boolean> {
        return try {
            val document = usersCollection.document(uid).get()
            Result.Success(document.exists)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to check profile existence: ${e.message}"))
        }
    }
}
