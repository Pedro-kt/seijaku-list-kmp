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
            is ApolloNetworkException -> ErrorType.NetworkError
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

    private fun checkMessage(message: String?): ErrorType {
        val msg = message?.lowercase() ?: ""
        return when {
            msg.contains("temporarily disabled") ||
            msg.contains("temporarily unavailable") -> ErrorType.ServerUnavailable
            msg.contains("network") || msg.contains("connection") -> ErrorType.NetworkError
            msg.contains("server") -> ErrorType.ServerError
            else -> ErrorType.UnknownError
        }
    }
}
