package com.yumedev.seijakulistkmp.features.search.presentation.model

data class RecentSearch(
    val id: Long,
    val query: String,
    val timestamp: Long
)
