package com.yumedev.seijakulistkmp.features.tracking.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.yumedev.seijakulistkmp.features.tracking.data.local.entity.MediaListEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: MediaListEntryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<MediaListEntryEntity>)

    @Query("SELECT * FROM media_list_entries WHERE media_id = :mediaId AND media_type = :mediaType LIMIT 1")
    suspend fun getEntryByMedia(mediaId: Int, mediaType: String): MediaListEntryEntity?

    @Query("SELECT * FROM media_list_entries WHERE media_id = :mediaId AND media_type = :mediaType LIMIT 1")
    fun observeEntryByMedia(mediaId: Int, mediaType: String): Flow<MediaListEntryEntity?>

    @Query("SELECT * FROM media_list_entries WHERE id = :entryId")
    suspend fun getEntryById(entryId: Long): MediaListEntryEntity?

    @Query("SELECT * FROM media_list_entries WHERE anilist_entry_id = :anilistEntryId LIMIT 1")
    suspend fun getEntryByAniListId(anilistEntryId: Int): MediaListEntryEntity?

    @Query("""
        SELECT * FROM media_list_entries
        WHERE media_type = :mediaType
        ORDER BY updated_at DESC
    """)
    fun observeEntriesByType(mediaType: String): Flow<List<MediaListEntryEntity>>

    @Query("""
        SELECT * FROM media_list_entries
        WHERE media_type = :mediaType AND status = :status
        ORDER BY updated_at DESC
    """)
    fun observeEntriesByTypeAndStatus(
        mediaType: String,
        status: String
    ): Flow<List<MediaListEntryEntity>>

    @Query("""
        SELECT * FROM media_list_entries
        WHERE media_type = :mediaType
        AND (:statusFilter IS NULL OR status IN (:statuses))
        ORDER BY
            CASE WHEN :sortBy = 'TITLE' THEN media_title END ASC,
            CASE WHEN :sortBy = 'SCORE' AND :ascending = 1 THEN score END ASC,
            CASE WHEN :sortBy = 'SCORE' AND :ascending = 0 THEN score END DESC,
            CASE WHEN :sortBy = 'PROGRESS' AND :ascending = 1 THEN progress END ASC,
            CASE WHEN :sortBy = 'PROGRESS' AND :ascending = 0 THEN progress END DESC,
            CASE WHEN :sortBy = 'UPDATED_AT' AND :ascending = 1 THEN updated_at END ASC,
            CASE WHEN :sortBy = 'UPDATED_AT' AND :ascending = 0 THEN updated_at END DESC
    """)
    fun observeEntriesFiltered(
        mediaType: String,
        statusFilter: Boolean? = null,
        statuses: List<String> = emptyList(),
        sortBy: String = "UPDATED_AT",
        ascending: Boolean = false
    ): Flow<List<MediaListEntryEntity>>

    @Query("SELECT * FROM media_list_entries WHERE media_title LIKE '%' || :query || '%'")
    fun searchEntries(query: String): Flow<List<MediaListEntryEntity>>

    @Query("SELECT * FROM media_list_entries")
    suspend fun getAllEntries(): List<MediaListEntryEntity>

    @Update
    suspend fun updateEntry(entry: MediaListEntryEntity)

    @Query("""
        UPDATE media_list_entries
        SET progress = :progress, updated_at = :updatedAt, needs_sync = 1
        WHERE media_id = :mediaId AND media_type = :mediaType
    """)
    suspend fun updateProgress(mediaId: Int, mediaType: String, progress: Int, updatedAt: Long)

    @Query("""
        UPDATE media_list_entries
        SET status = :status, updated_at = :updatedAt, needs_sync = 1
        WHERE media_id = :mediaId AND media_type = :mediaType
    """)
    suspend fun updateStatus(mediaId: Int, mediaType: String, status: String, updatedAt: Long)

    @Query("""
        UPDATE media_list_entries
        SET score = :score, updated_at = :updatedAt, needs_sync = 1
        WHERE media_id = :mediaId AND media_type = :mediaType
    """)
    suspend fun updateScore(mediaId: Int, mediaType: String, score: Float?, updatedAt: Long)

    @Delete
    suspend fun deleteEntry(entry: MediaListEntryEntity)

    @Query("DELETE FROM media_list_entries WHERE media_id = :mediaId AND media_type = :mediaType")
    suspend fun deleteByMedia(mediaId: Int, mediaType: String)

    @Query("DELETE FROM media_list_entries")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM media_list_entries WHERE media_type = :mediaType AND status = :status")
    fun getCountByStatus(mediaType: String, status: String): Flow<Int>

    @Query("SELECT * FROM media_list_entries WHERE needs_sync = 1")
    suspend fun getUnsyncedEntries(): List<MediaListEntryEntity>

    @Query("UPDATE media_list_entries SET is_synced = 1, needs_sync = 0 WHERE id = :entryId")
    suspend fun markAsSynced(entryId: Long)

    @Query("""
        UPDATE media_list_entries
        SET is_synced = 1, needs_sync = 0, anilist_entry_id = :anilistEntryId
        WHERE id = :entryId
    """)
    suspend fun updateSyncStatus(entryId: Long, anilistEntryId: Int?)

    @Query("UPDATE media_list_entries SET is_synced = 1, needs_sync = 0")
    suspend fun markAllAsSynced()
}
