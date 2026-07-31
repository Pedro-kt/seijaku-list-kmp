package com.yumedev.seijakulistkmp.core.domain.model

/**
 * Common pagination model for lists
 */
data class Pagination(
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val lastPage: Int? = null,
    val perPage: Int = 20,
    val total: Int? = null
)

/**
 * Wrapper for paginated data
 */
data class PaginatedData<T>(
    val data: List<T>,
    val pagination: Pagination
)
