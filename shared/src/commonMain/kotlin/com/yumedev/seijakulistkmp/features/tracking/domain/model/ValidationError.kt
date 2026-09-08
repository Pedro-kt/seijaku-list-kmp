package com.yumedev.seijakulistkmp.features.tracking.domain.model

sealed class ValidationError {
    data object ProgressNotZeroForPlanning : ValidationError()
    data object ProgressNotCompleteForCompleted : ValidationError()
    data object ProgressNegative : ValidationError()
    data class ProgressExceedsMaxAllowed(val maxAllowed: Int) : ValidationError()
    data object ProgressExceedsTotal : ValidationError()
}
