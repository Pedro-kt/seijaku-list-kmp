package com.yumedev.seijakulistkmp.core.error

import org.jetbrains.compose.resources.StringResource
import seijakulistkmp.shared.generated.resources.*

object ErrorUiMapper {
    fun mapToStringResource(errorType: ErrorType): StringResource {
        return when (errorType) {
            ErrorType.NetworkError -> Res.string.error_network
            ErrorType.TimeoutError -> Res.string.error_timeout
            ErrorType.ServerError -> Res.string.error_server
            ErrorType.ServerUnavailable -> Res.string.error_server_unavailable
            ErrorType.UnknownError -> Res.string.error_unknown
        }
    }
}
