package com.yumedev.seijakulistkmp.core.common.resource

/**
 * Sealed class to represent the state of a resource
 * Used in ViewModels to manage UI states
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val exception: Throwable? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
    data object Idle : Resource<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    val isLoading: Boolean
        get() = this is Loading

    val isIdle: Boolean
        get() = this is Idle

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrDefault(defaultValue: @UnsafeVariance T): T = when (this) {
        is Success -> data
        else -> defaultValue
    }

    fun getOrElse(onError: (Error) -> @UnsafeVariance T): T = when (this) {
        is Success -> data
        is Error -> onError(this)
        else -> throw IllegalStateException("Cannot get value from $this")
    }

    inline fun onSuccess(action: (T) -> Unit): Resource<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (Error) -> Unit): Resource<T> {
        if (this is Error) action(this)
        return this
    }

    inline fun onLoading(action: () -> Unit): Resource<T> {
        if (this is Loading) action()
        return this
    }
}
