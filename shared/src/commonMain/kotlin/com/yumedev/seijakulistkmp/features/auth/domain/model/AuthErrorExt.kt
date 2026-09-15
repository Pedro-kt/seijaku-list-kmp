package com.yumedev.seijakulistkmp.features.auth.domain.model

fun AuthError.toThrowable(): Throwable {
    return Exception(this.message)
}
