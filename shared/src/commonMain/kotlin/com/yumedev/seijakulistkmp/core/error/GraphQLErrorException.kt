package com.yumedev.seijakulistkmp.core.error

class GraphQLErrorException(
    message: String,
    val statusCode: Int
) : Exception(message)
