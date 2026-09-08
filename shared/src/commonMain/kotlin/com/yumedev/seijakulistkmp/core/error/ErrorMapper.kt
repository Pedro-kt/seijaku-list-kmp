package com.yumedev.seijakulistkmp.core.error

import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException

object ErrorMapper {
    fun mapToErrorType(exception: Throwable): ErrorType {
        val result = checkExceptionType(exception)
        if (result != null) return result

        exception.cause?.let { cause ->
            val causeResult = checkExceptionType(cause)
            if (causeResult != null) return causeResult
        }

        return checkMessage(exception.message)
    }

    private fun checkExceptionType(exception: Throwable): ErrorType? {
        return when (exception) {
            is ApolloNetworkException -> {
                val specificError = checkCauseChain(exception)
                specificError ?: checkMessage(exception.message)
            }
            is ApolloHttpException -> {
                when (exception.statusCode) {
                    403 -> ErrorType.ServerUnavailable
                    in 500..599 -> ErrorType.ServerError
                    else -> null
                }
            }
            is GraphQLErrorException -> {
                when {
                    exception.statusCode == 403 -> ErrorType.ServerUnavailable
                    exception.statusCode in 500..599 -> ErrorType.ServerError
                    else -> checkMessage(exception.message)
                }
            }
            else -> null
        }
    }

    private fun checkCauseChain(exception: Throwable): ErrorType? {
        var cause: Throwable? = exception.cause
        while (cause != null) {
            val className = cause::class.simpleName ?: ""
            val message = cause.message?.lowercase() ?: ""

            when {
                isTimeoutError(className, message) -> return ErrorType.TimeoutError
                isConnectionError(className, message) -> return ErrorType.NetworkError
            }

            cause = cause.cause
        }
        return null
    }

    private fun isTimeoutError(className: String, message: String): Boolean {
        return className.contains("TimeoutException", ignoreCase = true) ||
                message.contains("timeout") ||
                message.contains("timed out")
    }

    private fun isConnectionError(className: String, message: String): Boolean {
        return className.contains("UnknownHostException", ignoreCase = true) ||
                className.contains("ConnectException", ignoreCase = true) ||
                message.contains("no internet") ||
                message.contains("no network") ||
                message.contains("unable to resolve host")
    }

    private fun checkMessage(message: String?): ErrorType {
        val msg = message?.lowercase() ?: ""
        return when {
            msg.contains("timeout") || msg.contains("timed out") -> ErrorType.TimeoutError
            msg.contains("temporarily disabled") ||
            msg.contains("temporarily unavailable") -> ErrorType.ServerUnavailable
            msg.contains("network") || msg.contains("connection") -> ErrorType.NetworkError
            msg.contains("server") -> ErrorType.ServerError
            else -> ErrorType.UnknownError
        }
    }
}
