package com.yumedev.seijakulistkmp.features.tracking.data.remote

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.data.remote.dto.MediaListEntryDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FirestoreMediaListDataSource {

    private val firestore = Firebase.firestore
    private val usersCollection = firestore.collection("users")

    private fun getMediaListCollection(uid: String) =
        usersCollection.document(uid).collection("mediaList")

    suspend fun saveEntry(uid: String, entry: MediaListEntryDto): Result<String> {
        return try {
            val docRef = if (entry.id.isNotEmpty()) {
                getMediaListCollection(uid).document(entry.id)
            } else {
                getMediaListCollection(uid).document
            }

            docRef.set(entry.copy(id = docRef.id))
            Result.Success(docRef.id)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to save media list entry to Firestore: ${e.message}"))
        }
    }

    suspend fun updateEntry(uid: String, entryId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            getMediaListCollection(uid).document(entryId).update(updates)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to update media list entry in Firestore: ${e.message}"))
        }
    }

    suspend fun deleteEntry(uid: String, entryId: String): Result<Unit> {
        return try {
            getMediaListCollection(uid).document(entryId).delete()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to delete media list entry from Firestore: ${e.message}"))
        }
    }

    suspend fun getEntry(uid: String, entryId: String): Result<MediaListEntryDto?> {
        return try {
            val document = getMediaListCollection(uid).document(entryId).get()
            val entry = if (document.exists) {
                document.data<MediaListEntryDto>()
            } else {
                null
            }
            Result.Success(entry)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to get media list entry from Firestore: ${e.message}"))
        }
    }

    suspend fun getAllEntries(uid: String): Result<List<MediaListEntryDto>> {
        return try {
            val snapshot = getMediaListCollection(uid).get()
            val entries = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.data<MediaListEntryDto>()
                } catch (e: Exception) {
                    println("Error parsing media list entry: ${e.message}")
                    null
                }
            }
            Result.Success(entries)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to get all media list entries from Firestore: ${e.message}"))
        }
    }

    fun observeEntries(uid: String): Flow<List<MediaListEntryDto>> {
        return getMediaListCollection(uid)
            .snapshots
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.data<MediaListEntryDto>()
                    } catch (e: Exception) {
                        println("Error parsing media list entry: ${e.message}")
                        null
                    }
                }
            }
            .catch { e ->
                println("Error observing media list entries: ${e.message}")
                emit(emptyList())
            }
    }

    suspend fun deleteAllEntries(uid: String): Result<Unit> {
        return try {
            val snapshot = getMediaListCollection(uid).get()
            snapshot.documents.forEach { doc ->
                doc.reference.delete()
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to delete all media list entries: ${e.message}"))
        }
    }
}
