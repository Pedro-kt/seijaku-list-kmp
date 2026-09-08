package com.yumedev.seijakulistkmp.features.tracking.domain.model

import com.yumedev.seijakulistkmp.core.domain.model.MediaType

enum class MediaListStatus {
    CURRENT,
    COMPLETED,
    PLANNING,
    DROPPED,
    PAUSED,
    REPEATING;

    fun toMALStatus(mediaType: MediaType): String {
        return when (this) {
            CURRENT -> if (mediaType == MediaType.ANIME) "Watching" else "Reading"
            COMPLETED -> "Completed"
            PLANNING -> if (mediaType == MediaType.ANIME) "Plan to Watch" else "Plan to Read"
            DROPPED -> "Dropped"
            PAUSED -> "On-Hold"
            REPEATING -> if (mediaType == MediaType.ANIME) "Watching" else "Reading"
        }
    }

    companion object {
        fun fromMALStatus(malStatus: String): MediaListStatus {
            return when (malStatus) {
                "Watching", "Reading" -> CURRENT
                "Completed" -> COMPLETED
                "Plan to Watch", "Plan to Read" -> PLANNING
                "Dropped" -> DROPPED
                "On-Hold" -> PAUSED
                else -> PLANNING
            }
        }
    }
}
