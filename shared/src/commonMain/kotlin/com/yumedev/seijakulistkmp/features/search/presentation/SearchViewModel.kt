package com.yumedev.seijakulistkmp.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.data.remote.graphql.SearchAnimeQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.SearchMangaQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.SearchCharacterQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.SearchAnimeByGenreQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.SearchAnimeByEpisodesQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.GetCurrentSeasonAnimeQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.GetAiringTodayAnimeQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.GetTopRatedAnimeQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.GetRandomAnimeQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaSort
import com.yumedev.seijakulistkmp.data.remote.graphql.type.MediaSeason
import com.yumedev.seijakulistkmp.features.search.presentation.mapper.toSearchResultItems
import com.yumedev.seijakulistkmp.features.search.presentation.mapper.toCharacterResultItems
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaFormat
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaStatus
import com.yumedev.seijakulistkmp.features.search.presentation.model.QuickFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.RecentSearch
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.TrendingAnime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.toInstant

class SearchViewModel(
    private val apolloClient: ApolloClient,
    private val mediaStringFormatter: MediaStringFormatter
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        val now = Clock.System.now().toEpochMilliseconds()

        // Mock recent searches
        val mockRecentSearches = listOf(
            RecentSearch(1, "frieren", now - 3600000),
            RecentSearch(2, "berserk", now - 7200000),
            RecentSearch(3, "maomao", now - 10800000),
            RecentSearch(4, "one piece", now - 14400000)
        )

        // Mock trending animes
        val mockTrendingAnimes = listOf(
            TrendingAnime(
                id = 1,
                rank = 1,
                title = "Frieren: Beyond Journey's End",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx154587-n5xn4UGhKjhz.jpg",
                rating = 9.31,
                type = "TV",
                episodes = 28
            ),
            TrendingAnime(
                id = 2,
                rank = 2,
                title = "Fullmetal Alchemist: Brotherhood",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx6702-VI6KCbmFLAW6.jpg",
                rating = 9.09,
                type = "TV",
                episodes = 64
            ),
            TrendingAnime(
                id = 3,
                rank = 3,
                title = "Steins;Gate",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx9253-7pdcVzQSkKxT.jpg",
                rating = 9.07,
                type = "TV",
                episodes = 24
            ),
            TrendingAnime(
                id = 4,
                rank = 4,
                title = "Gintama°",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx20614-KK5sTdkIgJSo.jpg",
                rating = 9.06,
                type = "TV",
                episodes = 51
            ),
            TrendingAnime(
                id = 5,
                rank = 5,
                title = "Hunter x Hunter (2011)",
                coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx11061-0l5gpHD4TA2d.png",
                rating = 9.04,
                type = "TV",
                episodes = 148
            )
        )

        _state.update {
            it.copy(
                recentSearches = mockRecentSearches,
                trendingAnimes = mockTrendingAnimes
            )
        }
    }

    fun expandSearch() {
        _state.update { it.copy(isExpanded = true) }
    }

    fun collapseSearch() {
        _state.update { it.copy(isExpanded = false, searchQuery = "") }
    }

    fun setExpandedState(expanded: Boolean) {
        _state.update { it.copy(isExpanded = expanded) }
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun selectFilter(filter: SearchFilter) {
        _state.update {
            val shouldResetFormat = it.selectedFilter != filter &&
                    (filter == SearchFilter.ANIME || filter == SearchFilter.MANGA)

            it.copy(
                selectedFilter = filter,
                filterFormat = if (shouldResetFormat) MediaFormat.ALL else it.filterFormat
            )
        }
    }

    fun removeRecentSearch(search: RecentSearch) {
        _state.update {
            it.copy(recentSearches = it.recentSearches.filter { recent -> recent.id != search.id })
        }
    }

    fun addRecentSearch(query: String) {
        if (query.isBlank()) return

        val now = Clock.System.now().toEpochMilliseconds()
        val newSearch = RecentSearch(
            id = now,
            query = query,
            timestamp = now
        )

        _state.update {
            val updatedSearches = listOf(newSearch) + it.recentSearches.filter { recent ->
                recent.query.lowercase() != query.lowercase()
            }
            it.copy(recentSearches = updatedSearches.take(10))
        }
    }

    fun performSearch(query: String) {
        if (query.isBlank()) return

        addRecentSearch(query)

        val currentFilter = _state.value.selectedFilter

        viewModelScope.launch {
            _state.update { it.copy(isSearching = true, searchError = null, hasSearched = true) }

            try {
                when (currentFilter) {
                    SearchFilter.CHARACTERS -> {
                        val response = apolloClient
                            .query(
                                SearchCharacterQuery(
                                    search = query,
                                    page = Optional.present(1),
                                    perPage = Optional.present(20)
                                )
                            )
                            .execute()

                        response.exception?.let { throw it }
                        response.errors?.firstOrNull()?.let { error ->
                            throw Exception(error.message ?: "GraphQL error")
                        }

                        val characterResults = response.data?.Page?.characters?.toCharacterResultItems() ?: emptyList()

                        _state.update {
                            it.copy(
                                characterResults = characterResults,
                                searchResults = emptyList(),
                                isSearching = false,
                                searchError = null
                            )
                        }
                    }
                    SearchFilter.MANGA -> {
                        val response = apolloClient
                            .query(
                                SearchMangaQuery(
                                    search = query,
                                    page = Optional.present(1),
                                    perPage = Optional.present(20)
                                )
                            )
                            .execute()

                        response.exception?.let { throw it }
                        response.errors?.firstOrNull()?.let { error ->
                            throw Exception(error.message ?: "GraphQL error")
                        }

                        val results = response.data?.Page?.media?.toSearchResultItems(mediaStringFormatter) ?: emptyList()

                        _state.update {
                            it.copy(
                                searchResults = results,
                                characterResults = emptyList(),
                                isSearching = false,
                                searchError = null
                            )
                        }
                    }
                    SearchFilter.ANIME -> {
                        val response = apolloClient
                            .query(
                                SearchAnimeQuery(
                                    search = query,
                                    page = Optional.present(1),
                                    perPage = Optional.present(20)
                                )
                            )
                            .execute()

                        response.exception?.let { throw it }
                        response.errors?.firstOrNull()?.let { error ->
                            throw Exception(error.message ?: "GraphQL error")
                        }

                        val results = response.data?.Page?.media?.toSearchResultItems(mediaStringFormatter) ?: emptyList()

                        _state.update {
                            it.copy(
                                searchResults = results,
                                characterResults = emptyList(),
                                isSearching = false,
                                searchError = null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                val errorType = ErrorMapper.mapToErrorType(e)
                _state.update {
                    it.copy(
                        isSearching = false,
                        searchError = errorType
                    )
                }
            }
        }
    }

    fun retrySearch() {
        val currentQuery = _state.value.searchQuery
        if (currentQuery.isNotBlank()) {
            performSearch(currentQuery)
        }
    }

    fun clearSearchResults() {
        _state.update {
            it.copy(
                searchResults = emptyList(),
                characterResults = emptyList(),
                searchError = null,
                hasSearched = false,
                searchQuery = "" // Clear the query when going back
            )
        }
    }

    fun searchByMood(genres: List<String>, moodName: String) {
        searchByGenres(genres, moodName)
    }

    private fun searchByGenres(genres: List<String>, moodName: String) {
        // Handle Marathon mood (filter by episodes >= 75)
        if (genres.isEmpty()) {
            searchByEpisodeCount(minEpisodes = 75, moodName = moodName)
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSearching = true, searchError = null, hasSearched = true, searchQuery = moodName) }

            try {
                val response = apolloClient
                    .query(
                        SearchAnimeByGenreQuery(
                            genres = Optional.present(genres),
                            page = Optional.present(1),
                            perPage = Optional.present(20),
                            sort = Optional.present(listOf(MediaSort.TRENDING_DESC, MediaSort.POPULARITY_DESC))
                        )
                    )
                    .execute()

                response.exception?.let { throw it }
                response.errors?.firstOrNull()?.let { error ->
                    throw Exception(error.message ?: "GraphQL error")
                }

                val results = response.data?.Page?.media?.toSearchResultItems(mediaStringFormatter) ?: emptyList()

                _state.update {
                    it.copy(
                        searchResults = results,
                        isSearching = false,
                        searchError = null
                    )
                }
            } catch (e: Exception) {
                val errorType = ErrorMapper.mapToErrorType(e)
                _state.update {
                    it.copy(
                        isSearching = false,
                        searchError = errorType
                    )
                }
            }
        }
    }

    private fun searchByEpisodeCount(minEpisodes: Int, moodName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSearching = true, searchError = null, hasSearched = true, searchQuery = moodName) }

            try {
                val response = apolloClient
                    .query(
                        SearchAnimeByEpisodesQuery(
                            minEpisodes = Optional.present(minEpisodes),
                            page = Optional.present(1),
                            perPage = Optional.present(20),
                            sort = Optional.present(listOf(MediaSort.POPULARITY_DESC, MediaSort.SCORE_DESC))
                        )
                    )
                    .execute()

                response.exception?.let { throw it }
                response.errors?.firstOrNull()?.let { error ->
                    throw Exception(error.message ?: "GraphQL error")
                }

                val results = response.data?.Page?.media?.toSearchResultItems(mediaStringFormatter) ?: emptyList()

                _state.update {
                    it.copy(
                        searchResults = results,
                        isSearching = false,
                        searchError = null
                    )
                }
            } catch (e: Exception) {
                val errorType = ErrorMapper.mapToErrorType(e)
                _state.update {
                    it.copy(
                        isSearching = false,
                        searchError = errorType
                    )
                }
            }
        }
    }

    fun updateFilterStatus(status: MediaStatus) {
        _state.update { it.copy(filterStatus = status) }
    }

    fun updateFilterFormat(format: MediaFormat) {
        _state.update { it.copy(filterFormat = format) }
    }

    fun updateFilterMinScore(score: Int?) {
        _state.update { it.copy(filterMinScore = score) }
    }

    fun resetFilters() {
        _state.update {
            it.copy(
                filterStatus = MediaStatus.ALL,
                filterFormat = MediaFormat.ALL,
                filterMinScore = null
            )
        }
    }

    fun searchByQuickFilter(filter: QuickFilter, filterName: String) {
        when (filter) {
            QuickFilter.CurrentSeason -> searchCurrentSeason(filterName)
            QuickFilter.AiringToday -> searchAiringToday(filterName)
            QuickFilter.Top100 -> searchTop100(filterName)
            QuickFilter.Random -> searchRandom(filterName)
        }
    }

    private fun searchCurrentSeason(filterName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSearching = true, searchError = null, hasSearched = true, searchQuery = filterName) }

            try {
                val (season, year) = getCurrentSeason()

                val response = apolloClient
                    .query(
                        GetCurrentSeasonAnimeQuery(
                            season = Optional.present(season),
                            seasonYear = Optional.present(year),
                            page = Optional.present(1),
                            perPage = Optional.present(20)
                        )
                    )
                    .execute()

                response.exception?.let { throw it }
                response.errors?.firstOrNull()?.let { error ->
                    throw Exception(error.message ?: "GraphQL error")
                }

                val results = response.data?.Page?.media?.toSearchResultItems(mediaStringFormatter) ?: emptyList()

                _state.update {
                    it.copy(
                        searchResults = results,
                        isSearching = false,
                        searchError = null
                    )
                }
            } catch (e: Exception) {
                val errorType = ErrorMapper.mapToErrorType(e)
                _state.update {
                    it.copy(
                        isSearching = false,
                        searchError = errorType
                    )
                }
            }
        }
    }

    private fun searchAiringToday(filterName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSearching = true, searchError = null, hasSearched = true, searchQuery = filterName) }

            try {
                val (startOfDay, endOfDay) = getTodayTimestamps()

                val response = apolloClient
                    .query(
                        GetAiringTodayAnimeQuery(
                            airingAtGreater = Optional.present(startOfDay),
                            airingAtLesser = Optional.present(endOfDay),
                            page = Optional.present(1),
                            perPage = Optional.present(20)
                        )
                    )
                    .execute()

                response.exception?.let { throw it }
                response.errors?.firstOrNull()?.let { error ->
                    throw Exception(error.message ?: "GraphQL error")
                }

                val results = response.data?.Page?.airingSchedules?.toSearchResultItems(mediaStringFormatter) ?: emptyList()

                _state.update {
                    it.copy(
                        searchResults = results,
                        isSearching = false,
                        searchError = null
                    )
                }
            } catch (e: Exception) {
                val errorType = ErrorMapper.mapToErrorType(e)
                _state.update {
                    it.copy(
                        isSearching = false,
                        searchError = errorType
                    )
                }
            }
        }
    }

    private fun searchTop100(filterName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSearching = true, searchError = null, hasSearched = true, searchQuery = filterName) }

            try {
                val response = apolloClient
                    .query(
                        GetTopRatedAnimeQuery(
                            page = Optional.present(1),
                            perPage = Optional.present(100)
                        )
                    )
                    .execute()

                response.exception?.let { throw it }
                response.errors?.firstOrNull()?.let { error ->
                    throw Exception(error.message ?: "GraphQL error")
                }

                val results = response.data?.Page?.media?.toSearchResultItems(mediaStringFormatter) ?: emptyList()

                _state.update {
                    it.copy(
                        searchResults = results,
                        isSearching = false,
                        searchError = null
                    )
                }
            } catch (e: Exception) {
                val errorType = ErrorMapper.mapToErrorType(e)
                _state.update {
                    it.copy(
                        isSearching = false,
                        searchError = errorType
                    )
                }
            }
        }
    }

    private fun searchRandom(filterName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSearching = true, searchError = null, hasSearched = true, searchQuery = filterName) }

            try {
                val randomPage = (1..10).random()

                val response = apolloClient
                    .query(
                        GetRandomAnimeQuery(
                            page = Optional.present(randomPage),
                            perPage = Optional.present(20)
                        )
                    )
                    .execute()

                response.exception?.let { throw it }
                response.errors?.firstOrNull()?.let { error ->
                    throw Exception(error.message ?: "GraphQL error")
                }

                val results = response.data?.Page?.media?.toSearchResultItems(mediaStringFormatter) ?: emptyList()

                _state.update {
                    it.copy(
                        searchResults = results,
                        isSearching = false,
                        searchError = null
                    )
                }
            } catch (e: Exception) {
                val errorType = ErrorMapper.mapToErrorType(e)
                _state.update {
                    it.copy(
                        isSearching = false,
                        searchError = errorType
                    )
                }
            }
        }
    }

    private fun getCurrentSeason(): Pair<MediaSeason, Int> {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

        val year = localDateTime.year
        val month = localDateTime.monthNumber

        val season = when (month) {
            in 1..3 -> MediaSeason.WINTER
            in 4..6 -> MediaSeason.SPRING
            in 7..9 -> MediaSeason.SUMMER
            in 10..12 -> MediaSeason.FALL
            else -> MediaSeason.WINTER
        }

        return Pair(season, year)
    }

    private fun getTodayTimestamps(): Pair<Int, Int> {
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = now.toLocalDateTime(timeZone)

        val startOfDay = kotlinx.datetime.LocalDateTime(
            year = localDateTime.year,
            monthNumber = localDateTime.monthNumber,
            dayOfMonth = localDateTime.dayOfMonth,
            hour = 0,
            minute = 0,
            second = 0
        ).toInstant(timeZone).epochSeconds.toInt()

        val endOfDay = kotlinx.datetime.LocalDateTime(
            year = localDateTime.year,
            monthNumber = localDateTime.monthNumber,
            dayOfMonth = localDateTime.dayOfMonth,
            hour = 23,
            minute = 59,
            second = 59
        ).toInstant(timeZone).epochSeconds.toInt()

        return Pair(startOfDay, endOfDay)
    }
}
