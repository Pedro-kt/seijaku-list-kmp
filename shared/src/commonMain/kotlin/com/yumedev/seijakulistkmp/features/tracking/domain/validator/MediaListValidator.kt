package com.yumedev.seijakulistkmp.features.tracking.domain.validator

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ValidationError

object MediaListValidator {

    fun getMaxAllowedProgress(
        total: Int?,
        mediaStatus: String?
    ): Int? {
        if (total == null) return null

        return when (mediaStatus?.uppercase()) {
            "RELEASING", "PUBLISHING" -> {
                (total - 1).coerceAtLeast(0)
            }
            else -> {
                total
            }
        }
    }

    fun isStatusAllowed(
        listStatus: MediaListStatus,
        mediaStatus: String?
    ): Boolean {
        return when (mediaStatus?.uppercase()) {
            "NOT_YET_RELEASED" -> {
                listStatus == MediaListStatus.PLANNING
            }
            "RELEASING", "PUBLISHING" -> {
                listStatus != MediaListStatus.COMPLETED &&
                listStatus != MediaListStatus.REPEATING
            }
            "FINISHED" -> {
                true
            }
            "CANCELLED" -> {
                listStatus == MediaListStatus.PLANNING ||
                listStatus == MediaListStatus.PAUSED ||
                listStatus == MediaListStatus.DROPPED
            }
            else -> {
                true
            }
        }
    }

    fun getAvailableStatuses(
        mediaStatus: String?,
        mediaType: MediaType
    ): List<MediaListStatus> {
        return when (mediaStatus?.uppercase()) {
            "NOT_YET_RELEASED" -> {
                listOf(MediaListStatus.PLANNING)
            }
            "RELEASING", "PUBLISHING" -> {
                listOf(
                    MediaListStatus.CURRENT,
                    MediaListStatus.PLANNING,
                    MediaListStatus.PAUSED,
                    MediaListStatus.DROPPED
                )
            }
            "FINISHED" -> {
                listOf(
                    MediaListStatus.CURRENT,
                    MediaListStatus.COMPLETED,
                    MediaListStatus.PLANNING,
                    MediaListStatus.PAUSED,
                    MediaListStatus.DROPPED,
                    MediaListStatus.REPEATING
                )
            }
            "CANCELLED" -> {
                listOf(
                    MediaListStatus.PLANNING,
                    MediaListStatus.PAUSED,
                    MediaListStatus.DROPPED
                )
            }
            null, "" -> {
                listOf(
                    MediaListStatus.CURRENT,
                    MediaListStatus.COMPLETED,
                    MediaListStatus.PLANNING,
                    MediaListStatus.PAUSED,
                    MediaListStatus.DROPPED
                )
            }
            else -> {
                listOf(
                    MediaListStatus.CURRENT,
                    MediaListStatus.COMPLETED,
                    MediaListStatus.PLANNING,
                    MediaListStatus.PAUSED,
                    MediaListStatus.DROPPED
                )
            }
        }
    }

    fun isProgressValid(
        progress: Int,
        total: Int?,
        status: MediaListStatus
    ): Boolean {
        if (progress < 0) return false

        if (total != null && progress > total) return false

        if (status == MediaListStatus.PLANNING && progress > 0) return false

        if (status == MediaListStatus.COMPLETED && total != null && progress != total) return false

        return true
    }

    fun canIncrementProgress(
        currentStatus: MediaListStatus,
        currentProgress: Int,
        total: Int?,
        mediaStatus: String? = null
    ): Boolean {
        if (currentStatus != MediaListStatus.CURRENT &&
            currentStatus != MediaListStatus.REPEATING) {
            return false
        }

        val maxAllowed = getMaxAllowedProgress(total, mediaStatus) ?: total
        if (maxAllowed != null && currentProgress >= maxAllowed) {
            return false
        }

        return true
    }

    fun isScoreValid(score: Float?): Boolean {
        return score == null || score in 0f..10f
    }

    fun isNoteLengthValid(note: String): Boolean {
        return note.length <= 500
    }

    fun getAutoStatusOnCompletion(
        currentStatus: MediaListStatus,
        mediaStatus: String?
    ): MediaListStatus {
        return if (mediaStatus?.uppercase() == "FINISHED" ||
                   mediaStatus?.uppercase() == "RELEASING" ||
                   mediaStatus?.uppercase() == "PUBLISHING") {
            MediaListStatus.COMPLETED
        } else {
            currentStatus
        }
    }

    fun calculateStatusChangeAdjustments(
        previousStatus: MediaListStatus,
        newStatus: MediaListStatus,
        currentProgress: Int,
        totalProgress: Int?
    ): Pair<Int, Boolean> {
        if (previousStatus == MediaListStatus.COMPLETED &&
            (newStatus == MediaListStatus.CURRENT || newStatus == MediaListStatus.REPEATING)) {
            return Pair(0, true)
        }

        if (newStatus == MediaListStatus.PLANNING) {
            return Pair(0, false)
        }

        if (newStatus == MediaListStatus.COMPLETED && totalProgress != null) {
            return Pair(totalProgress, false)
        }

        return Pair(currentProgress, false)
    }

    fun validateStatusProgressConsistency(
        newStatus: MediaListStatus,
        newProgress: Int,
        total: Int?,
        mediaStatus: String? = null
    ): ValidationResult {
        if (newStatus == MediaListStatus.PLANNING && newProgress > 0) {
            return ValidationResult.Invalid(ValidationError.ProgressNotZeroForPlanning)
        }

        if (newStatus == MediaListStatus.COMPLETED && total != null && newProgress != total) {
            return ValidationResult.Invalid(ValidationError.ProgressNotCompleteForCompleted)
        }

        if (newProgress < 0) {
            return ValidationResult.Invalid(ValidationError.ProgressNegative)
        }

        val maxAllowed = getMaxAllowedProgress(total, mediaStatus) ?: total
        if (maxAllowed != null && newProgress > maxAllowed) {
            val error = when (mediaStatus?.uppercase()) {
                "RELEASING", "PUBLISHING" -> ValidationError.ProgressExceedsMaxAllowed(maxAllowed)
                else -> ValidationError.ProgressExceedsTotal
            }
            return ValidationResult.Invalid(error)
        }

        return ValidationResult.Valid
    }

    sealed class ValidationResult {
        data object Valid : ValidationResult()
        data class Invalid(val error: ValidationError) : ValidationResult()
    }
}
