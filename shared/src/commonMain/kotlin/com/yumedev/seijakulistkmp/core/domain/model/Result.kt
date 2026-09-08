package com.yumedev.seijakulistkmp.core.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Failure -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Failure -> throw exception
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

    inline fun onFailure(action: (Throwable) -> Unit): Result<T> {
        if (this is Failure) action(exception)
        return this
    }

    companion object {
        fun <T> success(value: T): Result<T> = Success(value)
        fun <T> failure(exception: Throwable): Result<T> = Failure(exception)
    }
}

inline fun <T> resultOf(block: () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: Throwable) {
    Result.Failure(e)
}
