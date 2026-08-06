package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.core.error.ErrorType
import com.yumedev.seijakulistkmp.features.home.presentation.model.ErrorUiModel
import org.jetbrains.compose.resources.getString
import seijakulistkmp.shared.generated.resources.*

class ErrorUiMapper {
    suspend fun mapFeaturedError(errorType: ErrorType): ErrorUiModel {
        return when (errorType) {
            ErrorType.ServerUnavailable -> ErrorUiModel(
                title = getString(Res.string.error_server_unavailable),
                hint = getString(Res.string.error_server_unavailable_hint)
            )
            else -> ErrorUiModel(
                title = getString(Res.string.error_loading_featured),
                hint = getString(Res.string.error_loading_featured_hint)
            )
        }
    }
}
