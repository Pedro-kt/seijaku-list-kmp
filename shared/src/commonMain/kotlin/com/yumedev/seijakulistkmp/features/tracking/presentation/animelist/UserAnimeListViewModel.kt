package com.yumedev.seijakulistkmp.features.tracking.presentation.animelist

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

class UserAnimeListViewModel(
    private val getMediaListUseCase: GetMediaListUseCase,
    private val getListStatsUseCase: GetListStatsUseCase,
    private val updateListEntryUseCase: UpdateListEntryUseCase,
    private val removeFromListUseCase: RemoveFromListUseCase,
    private val exportToMALUseCase: ExportToMALUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserAnimeListUiState())
    val uiState: StateFlow<UserAnimeListUiState> = _uiState.asStateFlow()

    init {
        loadAnimeList()
        loadStats()
    }

    fun onEvent(event: UserAnimeListEvent) {
        when (event) {
            is UserAnimeListEvent.FilterByStatus -> filterByStatus(event.status)
            is UserAnimeListEvent.SortBy -> sortBy(event.sortOption, event.ascending)
            is UserAnimeListEvent.Search -> search(event.query)
            is UserAnimeListEvent.RemoveEntry -> removeEntry(event.mediaId)
            is UserAnimeListEvent.IncrementProgress -> incrementProgress(event.mediaId)
            is UserAnimeListEvent.ChangeStatus -> changeStatus(event.mediaId, event.newStatus)
            is UserAnimeListEvent.EditEntry -> editEntry(event.entry)
            is UserAnimeListEvent.ExportToMAL -> exportToMAL()
            is UserAnimeListEvent.Refresh -> refresh()
            UserAnimeListEvent.ClearError -> clearError()
            UserAnimeListEvent.ClearSuccessMessage -> clearSuccessMessage()
            UserAnimeListEvent.DismissExportDialog -> dismissExportDialog()
        }
    }

    private fun loadAnimeList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getMediaListUseCase(MediaType.ANIME, _uiState.value.selectedStatus)
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
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            getListStatsUseCase(MediaType.ANIME).collect { stats ->
                _uiState.update { it.copy(stats = stats) }
            }
        }
    }

    private fun filterByStatus(status: MediaListStatus?) {
        _uiState.update { it.copy(selectedStatus = status) }
        loadAnimeList()
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

    private fun removeEntry(mediaId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = removeFromListUseCase(mediaId, MediaType.ANIME)) {
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

            when (val result = exportToMALUseCase(MediaType.ANIME)) {
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
        loadAnimeList()
        loadStats()
    }

    private fun incrementProgress(mediaId: Int) {
        viewModelScope.launch {
            val entry = _uiState.value.entries.find { it.mediaId == mediaId } ?: return@launch
            val maxProgress = entry.mediaInfo?.totalEpisodes

            if (maxProgress == null || entry.progress < maxProgress) {
                val newProgress = entry.progress + 1
                updateListEntryUseCase(
                    mediaId = mediaId,
                    mediaType = MediaType.ANIME,
                    progress = newProgress
                )
            }
        }
    }

    private fun changeStatus(mediaId: Int, newStatus: MediaListStatus) {
        viewModelScope.launch {
            when (val result = updateListEntryUseCase(
                mediaId = mediaId,
                mediaType = MediaType.ANIME,
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

data class UserAnimeListUiState(
    val entries: List<MediaListEntry> = emptyList(),
    val filteredEntries: List<MediaListEntry> = emptyList(),
    val selectedStatus: MediaListStatus? = null,
    val stats: MediaListStats? = null,
    val sortBy: MediaListSortOption = MediaListSortOption.UPDATED_AT,
    val ascending: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val isExporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val exportedXml: String? = null,
    val error: String? = null,
    val successMessage: String? = null
)

sealed class UserAnimeListEvent {
    data class FilterByStatus(val status: MediaListStatus?) : UserAnimeListEvent()
    data class SortBy(val sortOption: MediaListSortOption, val ascending: Boolean) : UserAnimeListEvent()
    data class Search(val query: String) : UserAnimeListEvent()
    data class RemoveEntry(val mediaId: Int) : UserAnimeListEvent()
    data class IncrementProgress(val mediaId: Int) : UserAnimeListEvent()
    data class ChangeStatus(val mediaId: Int, val newStatus: MediaListStatus) : UserAnimeListEvent()
    data class EditEntry(val entry: MediaListEntry) : UserAnimeListEvent()
    data object ExportToMAL : UserAnimeListEvent()
    data object Refresh : UserAnimeListEvent()
    data object ClearError : UserAnimeListEvent()
    data object ClearSuccessMessage : UserAnimeListEvent()
    data object DismissExportDialog : UserAnimeListEvent()
}
