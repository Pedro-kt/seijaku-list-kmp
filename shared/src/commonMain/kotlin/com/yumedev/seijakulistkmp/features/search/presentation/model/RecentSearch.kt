package com.yumedev.seijakulistkmp.features.search.presentation.model

data class RecentSearch(
    val id: Long,
    val query: String,
    val searchType: SearchFilter,
    val timestamp: Long
)
