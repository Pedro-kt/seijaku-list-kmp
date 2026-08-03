package com.yumedev.seijakulistkmp.features.search.data

import com.russhwolf.settings.Settings
import com.yumedev.seijakulistkmp.features.search.presentation.model.RecentSearch
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RecentSearchRepository(
    private val settings: Settings,
    private val json: Json
) {
    private val _recentSearches = MutableStateFlow<List<RecentSearch>>(emptyList())
    val recentSearches: StateFlow<List<RecentSearch>> = _recentSearches.asStateFlow()

    private companion object {
        const val KEY_RECENT_SEARCHES = "recent_searches"
        const val MAX_RECENT_SEARCHES = 10
    }

    init {
        loadRecentSearches()
    }

    private fun loadRecentSearches() {
        val savedSearches = settings.getStringOrNull(KEY_RECENT_SEARCHES)
        if (savedSearches != null) {
            try {
                val searches = json.decodeFromString<List<RecentSearchData>>(savedSearches)
                _recentSearches.value = searches.map { it.toRecentSearch() }
            } catch (e: Exception) {
                settings.remove(KEY_RECENT_SEARCHES)
                _recentSearches.value = emptyList()
            }
        }
    }

    fun addRecentSearch(query: String, searchType: SearchFilter, timestamp: Long) {
        if (query.isBlank()) return

        val currentSearches = _recentSearches.value.toMutableList()

        currentSearches.removeAll { it.query.equals(query, ignoreCase = true) }

        val newSearch = RecentSearch(
            id = timestamp,
            query = query,
            searchType = searchType,
            timestamp = timestamp
        )
        currentSearches.add(0, newSearch)

        val updatedSearches = currentSearches.take(MAX_RECENT_SEARCHES)

        _recentSearches.value = updatedSearches
        saveRecentSearches(updatedSearches)
    }

    fun removeRecentSearch(searchId: Long) {
        val updatedSearches = _recentSearches.value.filter { it.id != searchId }
        _recentSearches.value = updatedSearches
        saveRecentSearches(updatedSearches)
    }

    fun clearAllRecentSearches() {
        _recentSearches.value = emptyList()
        settings.remove(KEY_RECENT_SEARCHES)
    }

    private fun saveRecentSearches(searches: List<RecentSearch>) {
        val data = searches.map { RecentSearchData.fromRecentSearch(it) }
        val jsonString = json.encodeToString(data)
        settings.putString(KEY_RECENT_SEARCHES, jsonString)
    }
}

@kotlinx.serialization.Serializable
private data class RecentSearchData(
    val id: Long,
    val query: String,
    val searchType: String,
    val timestamp: Long
) {
    fun toRecentSearch(): RecentSearch {
        val filter = when (searchType) {
            "ANIME" -> SearchFilter.ANIME
            "MANGA" -> SearchFilter.MANGA
            "CHARACTERS" -> SearchFilter.CHARACTERS
            else -> SearchFilter.ANIME
        }
        return RecentSearch(
            id = id,
            query = query,
            searchType = filter,
            timestamp = timestamp
        )
    }

    companion object {
        fun fromRecentSearch(search: RecentSearch): RecentSearchData {
            return RecentSearchData(
                id = search.id,
                query = search.query,
                searchType = when (search.searchType) {
                    SearchFilter.ANIME -> "ANIME"
                    SearchFilter.MANGA -> "MANGA"
                    SearchFilter.CHARACTERS -> "CHARACTERS"
                },
                timestamp = search.timestamp
            )
        }
    }
}
