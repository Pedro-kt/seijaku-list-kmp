package com.yumedev.seijakulistkmp.core.error

sealed class ErrorType {
    data object NetworkError : ErrorType()
    data object ServerUnavailable : ErrorType()
    data object ServerError : ErrorType()
    data object UnknownError : ErrorType()
}
