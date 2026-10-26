package com.yumedev.seijakulistkmp.features.profile.data.remote

import com.yumedev.seijakulistkmp.core.domain.model.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.Data
import dev.gitlive.firebase.storage.storage

class FirebaseStorageDataSource {
    private val storage = Firebase.storage
    private val profileImagesRef = storage.reference.child("userProfiles")

    suspend fun uploadAvatar(userId: String, imageData: ByteArray): Result<String> {
        return try {
            val fileName = "avatar_${userId}_${System.currentTimeMillis()}.jpg"
            val avatarRef = profileImagesRef.child(userId).child(fileName)

            avatarRef.putData(Data(imageData))

            val downloadUrl = avatarRef.getDownloadUrl()
            Result.Success(downloadUrl)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to upload avatar: ${e.message}"))
        }
    }

    suspend fun uploadBanner(userId: String, imageData: ByteArray): Result<String> {
        return try {
            val fileName = "banner_${userId}_${System.currentTimeMillis()}.jpg"
            val bannerRef = profileImagesRef.child(userId).child(fileName)

            bannerRef.putData(Data(imageData))

            val downloadUrl = bannerRef.getDownloadUrl()
            Result.Success(downloadUrl)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to upload banner: ${e.message}"))
        }
    }

    suspend fun deleteImage(imageUrl: String): Result<Unit> {
        return try {
            val imageRef = storage.reference(imageUrl)
            imageRef.delete()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to delete image: ${e.message}"))
        }
    }

    suspend fun deleteUserImages(userId: String): Result<Unit> {
        return try {
            val userImagesRef = profileImagesRef.child(userId)
            val listResult = userImagesRef.listAll()

            listResult.items.forEach { item ->
                item.delete()
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to delete user images: ${e.message}"))
        }
    }
}
