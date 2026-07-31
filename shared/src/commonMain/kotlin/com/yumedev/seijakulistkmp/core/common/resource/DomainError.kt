package com.yumedev.seijakulistkmp.core.common.resource

/**
 * Sealed class representing domain-level errors
 */
sealed class DomainError(open val message: String) {
    data class Network(override val message: String = "Network error occurred") : DomainError(message)
    data class NotFound(override val message: String = "Resource not found") : DomainError(message)
    data class Unauthorized(override val message: String = "Unauthorized access") : DomainError(message)
    data class ServerError(override val message: String = "Server error occurred") : DomainError(message)
    data class ValidationError(override val message: String = "Validation failed") : DomainError(message)
    data class CacheError(override val message: String = "Cache operation failed") : DomainError(message)
    data class Unknown(override val message: String = "Unknown error occurred") : DomainError(message)

    fun toException(): Exception = when (this) {
        is Network -> NetworkException(message)
        is NotFound -> NotFoundException(message)
        is Unauthorized -> UnauthorizedException(message)
        is ServerError -> ServerException(message)
        is ValidationError -> ValidationException(message)
        is CacheError -> CacheException(message)
        is Unknown -> UnknownException(message)
    }
}

// Domain Exceptions
class NetworkException(message: String) : Exception(message)
class NotFoundException(message: String) : Exception(message)
class UnauthorizedException(message: String) : Exception(message)
class ServerException(message: String) : Exception(message)
class ValidationException(message: String) : Exception(message)
class CacheException(message: String) : Exception(message)
class UnknownException(message: String) : Exception(message)
