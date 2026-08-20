package com.yumedev.seijakulistkmp.features.tracking.presentation.mangalist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListSortOption
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStats
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ExportToMALUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetListStatsUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetMediaListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.RemoveFromListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.UpdateListEntryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserMangaListViewModel(
    private val getMediaListUseCase: GetMediaListUseCase,
    private val getListStatsUseCase: GetListStatsUseCase,
    private val updateListEntryUseCase: UpdateListEntryUseCase,
    private val removeFromListUseCase: RemoveFromListUseCase,
    private val exportToMALUseCase: ExportToMALUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserMangaListUiState())
    val uiState: StateFlow<UserMangaListUiState> = _uiState.asStateFlow()

    init {
        loadMangaList()
        loadStats()
    }

    fun onEvent(event: UserMangaListEvent) {
        when (event) {
            is UserMangaListEvent.FilterByStatus -> filterByStatus(event.status)
            is UserMangaListEvent.SortBy -> sortBy(event.sortOption, event.ascending)
            is UserMangaListEvent.Search -> search(event.query)
            UserMangaListEvent.ToggleSearch -> toggleSearch()
            UserMangaListEvent.HideSearch -> hideSearch()
            is UserMangaListEvent.RemoveEntry -> removeEntry(event.mediaId)
            is UserMangaListEvent.IncrementProgress -> incrementProgress(event.mediaId)
            is UserMangaListEvent.ChangeStatus -> changeStatus(event.mediaId, event.newStatus)
            is UserMangaListEvent.EditEntry -> editEntry(event.entry)
            is UserMangaListEvent.ExportToMAL -> exportToMAL()
            is UserMangaListEvent.Refresh -> refresh()
            UserMangaListEvent.ClearError -> clearError()
            UserMangaListEvent.ClearSuccessMessage -> clearSuccessMessage()
            UserMangaListEvent.DismissExportDialog -> dismissExportDialog()
        }
    }

    private fun loadMangaList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getMediaListUseCase(MediaType.MANGA, _uiState.value.selectedStatus)
                .collect { entries ->
                    val sorted = sortEntries(
                        entries,
                        _uiState.value.sortBy,
                        _uiState.value.ascending
                    )
                    val filtered = filterEntries(sorted, _uiState.value.searchQuery)

                    _uiState.update {
                        it.copy(
                            entries = sorted,
                            filteredEntries = filtered,
                            isLoading = false,
                            isSearchVisible = if (sorted.isEmpty()) false else it.isSearchVisible,
                            searchQuery = if (sorted.isEmpty()) "" else it.searchQuery
                        )
                    }
                }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            getListStatsUseCase(MediaType.MANGA).collect { stats ->
                _uiState.update { it.copy(stats = stats) }
            }
        }
    }

    private fun filterByStatus(status: MediaListStatus?) {
        _uiState.update { it.copy(selectedStatus = status) }
        loadMangaList()
    }

    private fun sortBy(sortOption: MediaListSortOption, ascending: Boolean) {
        _uiState.update {
            it.copy(
                sortBy = sortOption,
                ascending = ascending,
                entries = sortEntries(it.entries, sortOption, ascending),
                filteredEntries = sortEntries(it.filteredEntries, sortOption, ascending)
            )
        }
    }

    private fun search(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredEntries = filterEntries(it.entries, query)
            )
        }
    }

    private fun toggleSearch() {
        _uiState.update {
            it.copy(
                isSearchVisible = !it.isSearchVisible,
                searchQuery = if (!it.isSearchVisible) "" else it.searchQuery,
                filteredEntries = if (!it.isSearchVisible) it.entries else filterEntries(it.entries, it.searchQuery)
            )
        }
    }

    private fun hideSearch() {
        _uiState.update {
            it.copy(
                isSearchVisible = false,
                searchQuery = "",
                filteredEntries = it.entries
            )
        }
    }

    private fun removeEntry(mediaId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = removeFromListUseCase(mediaId, MediaType.MANGA)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "list_deleted_success"
                        )
                    }
                }
                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to remove entry"
                        )
                    }
                }
            }
        }
    }

    private fun exportToMAL() {
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true, error = null) }

            when (val result = exportToMALUseCase(MediaType.MANGA)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isExporting = false,
                            exportSuccess = true,
                            exportedXml = result.data
                        )
                    }
                }
                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isExporting = false,
                            error = result.exception.message ?: "Failed to export to MAL"
                        )
                    }
                }
            }
        }
    }

    private fun refresh() {
        loadMangaList()
        loadStats()
    }

    private fun incrementProgress(mediaId: Int) {
        viewModelScope.launch {
            val entry = _uiState.value.entries.find { it.mediaId == mediaId } ?: return@launch
            val maxProgress = entry.mediaInfo?.totalChapters

            if (maxProgress == null || entry.progress < maxProgress) {
                val newProgress = entry.progress + 1
                updateListEntryUseCase(
                    mediaId = mediaId,
                    mediaType = MediaType.MANGA,
                    progress = newProgress
                )
            }
        }
    }

    private fun changeStatus(mediaId: Int, newStatus: MediaListStatus) {
        viewModelScope.launch {
            when (val result = updateListEntryUseCase(
                mediaId = mediaId,
                mediaType = MediaType.MANGA,
                status = newStatus
            )) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(successMessage = "list_status_updated")
                    }
                }
                is Result.Failure -> {
                    _uiState.update {
                        it.copy(error = result.exception.message ?: "Failed to update status")
                    }
                }
            }
        }
    }

    private fun editEntry(entry: MediaListEntry) {
        // TODO: Show edit bottom sheet
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    private fun dismissExportDialog() {
        _uiState.update {
            it.copy(
                exportSuccess = false,
                exportedXml = null
            )
        }
    }

    private fun sortEntries(
        entries: List<MediaListEntry>,
        sortBy: MediaListSortOption,
        ascending: Boolean
    ): List<MediaListEntry> {
        val sorted = when (sortBy) {
            MediaListSortOption.TITLE -> entries.sortedBy { it.mediaInfo?.title?.lowercase() }
            MediaListSortOption.SCORE -> entries.sortedBy { it.score ?: 0f }
            MediaListSortOption.PROGRESS -> entries.sortedBy { it.progress }
            MediaListSortOption.UPDATED_AT -> entries.sortedBy { it.updatedAt }
        }
        return if (ascending) sorted else sorted.reversed()
    }

    private fun filterEntries(
        entries: List<MediaListEntry>,
        query: String
    ): List<MediaListEntry> {
        if (query.isBlank()) return entries

        return entries.filter { entry ->
            entry.mediaInfo?.title?.contains(query, ignoreCase = true) == true
        }
    }
}

data class UserMangaListUiState(
    val entries: List<MediaListEntry> = emptyList(),
    val filteredEntries: List<MediaListEntry> = emptyList(),
    val selectedStatus: MediaListStatus? = null,
    val stats: MediaListStats? = null,
    val sortBy: MediaListSortOption = MediaListSortOption.UPDATED_AT,
    val ascending: Boolean = false,
    val searchQuery: String = "",
    val isSearchVisible: Boolean = false,
    val isLoading: Boolean = true,
    val isExporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val exportedXml: String? = null,
    val error: String? = null,
    val successMessage: String? = null
)

sealed class UserMangaListEvent {
    data class FilterByStatus(val status: MediaListStatus?) : UserMangaListEvent()
    data class SortBy(val sortOption: MediaListSortOption, val ascending: Boolean) : UserMangaListEvent()
    data class Search(val query: String) : UserMangaListEvent()
    data object ToggleSearch : UserMangaListEvent()
    data object HideSearch : UserMangaListEvent()
    data class RemoveEntry(val mediaId: Int) : UserMangaListEvent()
    data class IncrementProgress(val mediaId: Int) : UserMangaListEvent()
    data class ChangeStatus(val mediaId: Int, val newStatus: MediaListStatus) : UserMangaListEvent()
    data class EditEntry(val entry: MediaListEntry) : UserMangaListEvent()
    data object ExportToMAL : UserMangaListEvent()
    data object Refresh : UserMangaListEvent()
    data object ClearError : UserMangaListEvent()
    data object ClearSuccessMessage : UserMangaListEvent()
    data object DismissExportDialog : UserMangaListEvent()
}
