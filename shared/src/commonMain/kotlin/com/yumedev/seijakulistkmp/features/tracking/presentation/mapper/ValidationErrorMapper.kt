package com.yumedev.seijakulistkmp.features.tracking.presentation.mapper

import androidx.compose.runtime.Composable
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ValidationError
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun ValidationError.toLocalizedMessage(): String {
    return when (this) {
        is ValidationError.ProgressNotZeroForPlanning ->
            stringResource(Res.string.validation_progress_not_zero_for_planning)

        is ValidationError.ProgressNotCompleteForCompleted ->
            stringResource(Res.string.validation_progress_not_complete_for_completed)

        is ValidationError.ProgressNegative ->
            stringResource(Res.string.validation_progress_negative)

        is ValidationError.ProgressExceedsMaxAllowed ->
            stringResource(Res.string.validation_progress_exceeds_max_allowed, maxAllowed)

        is ValidationError.ProgressExceedsTotal ->
            stringResource(Res.string.validation_progress_exceeds_total)
    }
}
