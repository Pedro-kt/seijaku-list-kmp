package com.yumedev.seijakulistkmp.core.common.resource

/**
 * Sealed class representing domain-level errors
 */
sealed class DomainError(open val message: String, open val cause: Throwable? = null) {
    data class Network(override val message: String = "Network error occurred", override val cause: Throwable? = null) : DomainError(message, cause)
    data class NotFound(override val message: String = "Resource not found", override val cause: Throwable? = null) : DomainError(message, cause)
    data class Unauthorized(override val message: String = "Unauthorized access", override val cause: Throwable? = null) : DomainError(message, cause)
    data class ServerError(override val message: String = "Server error occurred", override val cause: Throwable? = null) : DomainError(message, cause)
    data class ValidationError(override val message: String = "Validation failed", override val cause: Throwable? = null) : DomainError(message, cause)
    data class CacheError(override val message: String = "Cache operation failed", override val cause: Throwable? = null) : DomainError(message, cause)
    data class Unknown(override val message: String = "Unknown error occurred", override val cause: Throwable? = null) : DomainError(message, cause)

    fun toException(): Exception = when (this) {
        is Network -> NetworkException(message, cause)
        is NotFound -> NotFoundException(message, cause)
        is Unauthorized -> UnauthorizedException(message, cause)
        is ServerError -> ServerException(message, cause)
        is ValidationError -> ValidationException(message, cause)
        is CacheError -> CacheException(message, cause)
        is Unknown -> UnknownException(message, cause)
    }
}

// Domain Exceptions
class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)
class NotFoundException(message: String, cause: Throwable? = null) : Exception(message, cause)
class UnauthorizedException(message: String, cause: Throwable? = null) : Exception(message, cause)
class ServerException(message: String, cause: Throwable? = null) : Exception(message, cause)
class ValidationException(message: String, cause: Throwable? = null) : Exception(message, cause)
class CacheException(message: String, cause: Throwable? = null) : Exception(message, cause)
class UnknownException(message: String, cause: Throwable? = null) : Exception(message, cause)
