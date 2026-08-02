package com.yumedev.seijakulistkmp.core.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import com.yumedev.seijakulistkmp.core.common.resource.Resource
import com.yumedev.seijakulistkmp.core.common.resource.Result
import com.yumedev.seijakulistkmp.core.common.resource.DomainError

fun <T> Flow<Result<T>>.asResource(): Flow<Resource<T>> = this
    .map { result ->
        when (result) {
            is Result.Success -> Resource.Success(result.data)
            is Result.Failure -> Resource.Error(result.error.message, result.error.cause ?: result.error.toException())
        }
    }
    .onStart { emit(Resource.Loading) }
    .catch { exception ->
        emit(Resource.Error(exception.message ?: "Unknown error", exception))
    }

suspend fun <T> safeCall(block: suspend () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: Exception) {
    Result.Failure(DomainError.Unknown(e.message ?: "Unknown error", e))
}

fun <T, R> T?.mapIfNotNull(transform: (T) -> R): R? = this?.let(transform)
