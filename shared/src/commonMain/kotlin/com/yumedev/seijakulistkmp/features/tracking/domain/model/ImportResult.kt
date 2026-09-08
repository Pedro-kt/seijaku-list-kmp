package com.yumedev.seijakulistkmp.features.tracking.domain.model

import com.yumedev.seijakulistkmp.core.domain.model.MediaType

data class ImportResult(
    val successful: List<MediaListEntry>,
    val conflicts: List<ImportConflict>,
    val errors: List<ImportError>,
    val totalProcessed: Int
) {
    val successCount: Int get() = successful.size
    val conflictCount: Int get() = conflicts.size
    val errorCount: Int get() = errors.size
    val hasConflicts: Boolean get() = conflicts.isNotEmpty()
    val hasErrors: Boolean get() = errors.isNotEmpty()
}

data class ImportConflict(
    val mediaId: Int,
    val mediaType: MediaType,
    val localEntry: MediaListEntry,
    val importedEntry: MediaListEntry,
    val conflictReason: ConflictReason
)

enum class ConflictReason {
    ALREADY_EXISTS,
    DIFFERENT_PROGRESS,
    DIFFERENT_STATUS,
    NEWER_LOCAL_UPDATE,
    NEWER_IMPORT_UPDATE
}

data class ImportError(
    val mediaId: Int?,
    val mediaType: MediaType?,
    val reason: ErrorReason,
    val message: String,
    val xmlEntryIndex: Int?
)

enum class ErrorReason {
    INVALID_DATA,
    VALIDATION_FAILED,
    MISSING_REQUIRED_FIELD,
    PARSE_ERROR,
    UNKNOWN
}

sealed class ConflictResolution {
    data object KeepLocal : ConflictResolution()
    data object UseImported : ConflictResolution()
    data class MergeFields(
        val useLocalProgress: Boolean = false,
        val useLocalScore: Boolean = false,
        val useLocalStatus: Boolean = false,
        val useLocalNotes: Boolean = false,
        val useLocalDates: Boolean = false
    ) : ConflictResolution()
}
