package com.yumedev.seijakulistkmp.core.common.resource

/**
 * Result wrapper for data layer operations
 * Used in repositories and data sources to handle success/failure
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: DomainError) : Result<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isFailure: Boolean
        get() = this is Failure

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Failure -> null
    }

    fun getOrDefault(defaultValue: @UnsafeVariance T): T = when (this) {
        is Success -> data
        is Failure -> defaultValue
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Failure -> throw error.toException()
    }

    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Failure -> this
    }

    inline fun <R> flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
        is Success -> transform(data)
        is Failure -> this
    }

    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onFailure(action: (DomainError) -> Unit): Result<T> {
        if (this is Failure) action(error)
        return this
    }

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun failure(error: DomainError): Result<Nothing> = Failure(error)
        fun failure(message: String): Result<Nothing> = Failure(DomainError.Unknown(message))
    }
}

/**
 * Extension to convert Result to Resource
 */
fun <T> Result<T>.toResource(): Resource<T> = when (this) {
    is Result.Success -> Resource.Success(data)
    is Result.Failure -> Resource.Error(error.message, error.toException())
}
