package com.yumedev.seijakulistkmp.features.detail.data.remote

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.detail.data.remote.dto.FavoriteEpisodeDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FirestoreFavoriteEpisodeDataSource {

    private val firestore = Firebase.firestore
    private val usersCollection = firestore.collection("users")

    private fun getFavoriteEpisodesCollection(uid: String) =
        usersCollection.document(uid).collection("favoriteEpisodes")

    private fun getDocumentId(mediaId: Int, episodeNumber: Int): String {
        return "${mediaId}_${episodeNumber}"
    }

    suspend fun saveEpisode(uid: String, episode: FavoriteEpisodeDto): Result<Unit> {
        return try {
            val docId = getDocumentId(episode.mediaId, episode.episodeNumber)
            val docRef = getFavoriteEpisodesCollection(uid).document(docId)

            val episodeToSave = episode.copy(
                syncedAt = System.currentTimeMillis()
            )

            docRef.set(episodeToSave)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to save favorite episode to Firestore: ${e.message}"))
        }
    }

    suspend fun deleteEpisode(uid: String, mediaId: Int, episodeNumber: Int): Result<Unit> {
        return try {
            val docId = getDocumentId(mediaId, episodeNumber)
            getFavoriteEpisodesCollection(uid).document(docId).delete()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to delete favorite episode from Firestore: ${e.message}"))
        }
    }

    suspend fun getEpisode(uid: String, mediaId: Int, episodeNumber: Int): Result<FavoriteEpisodeDto?> {
        return try {
            val docId = getDocumentId(mediaId, episodeNumber)
            val document = getFavoriteEpisodesCollection(uid).document(docId).get()
            val episode = if (document.exists) {
                document.data<FavoriteEpisodeDto>()
            } else {
                null
            }
            Result.Success(episode)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to get favorite episode from Firestore: ${e.message}"))
        }
    }

    suspend fun getAllEpisodes(uid: String): Result<List<FavoriteEpisodeDto>> {
        return try {
            val snapshot = getFavoriteEpisodesCollection(uid).get()
            val episodes = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.data<FavoriteEpisodeDto>()
                } catch (e: Exception) {
                    println("Error parsing favorite episode: ${e.message}")
                    null
                }
            }
            Result.Success(episodes)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to get all favorite episodes from Firestore: ${e.message}"))
        }
    }

    fun observeEpisodes(uid: String): Flow<List<FavoriteEpisodeDto>> {
        return getFavoriteEpisodesCollection(uid)
            .snapshots
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.data<FavoriteEpisodeDto>()
                    } catch (e: Exception) {
                        println("Error parsing favorite episode: ${e.message}")
                        null
                    }
                }
            }
            .catch { e ->
                println("Error observing favorite episodes: ${e.message}")
                emit(emptyList())
            }
    }
}
