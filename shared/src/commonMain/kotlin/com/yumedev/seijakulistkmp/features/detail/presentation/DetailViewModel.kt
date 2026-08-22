package com.yumedev.seijakulistkmp.features.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetAnimeDetailUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetMangaDetailUseCase
import com.yumedev.seijakulistkmp.features.detail.presentation.utils.toCore
import com.yumedev.seijakulistkmp.features.tracking.domain.model.CachedMediaInfo
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.AddToListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.CheckInListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetListEntryUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.UpdateListEntryUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val getMangaDetailUseCase: GetMangaDetailUseCase,
    private val addToListUseCase: AddToListUseCase,
    private val updateListEntryUseCase: UpdateListEntryUseCase,
    private val checkInListUseCase: CheckInListUseCase,
    private val getListEntryUseCase: GetListEntryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private var observeListEntryJob: Job? = null

    fun loadMediaDetail(id: Int, type: MediaType) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = when (type) {
                MediaType.ANIME -> getAnimeDetailUseCase(id)
                MediaType.MANGA -> getMangaDetailUseCase(id)
            }

            result.onSuccess { mediaDetail ->
                val isInList = checkInListUseCase(id, type.toCore())

                _state.update {
                    it.copy(
                        mediaDetail = mediaDetail.copy(isInList = isInList),
                        isLoading = false,
                        error = null
                    )
                }

                observeListEntry(id, type)
            }.onFailure { exception ->
                val errorType = ErrorMapper.mapToErrorType(exception)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = errorType
                    )
                }
            }
        }
    }

    private fun observeListEntry(id: Int, type: MediaType) {
        observeListEntryJob?.cancel()
        observeListEntryJob = getListEntryUseCase.observe(id, type.toCore())
            .onEach { entry ->
                _state.update { currentState ->
                    currentState.copy(
                        listEntry = entry,
                        mediaDetail = currentState.mediaDetail?.copy(isInList = entry != null)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun retry(id: Int, type: MediaType) {
        loadMediaDetail(id, type)
    }

    fun toggleFavorite() {
        _state.update { currentState ->
            currentState.copy(
                mediaDetail = currentState.mediaDetail?.copy(
                    isFavorite = !currentState.mediaDetail.isFavorite
                )
            )
        }
    }

    fun saveToList(
        status: MediaListStatus,
        progress: Int,
        score: Float?,
        note: String,
        startDate: String?,
        rewatches: Int,
        priority: MediaListPriority
    ) {
        viewModelScope.launch {
            val currentDetail = _state.value.mediaDetail ?: return@launch
            val mediaType = currentDetail.type.toCore()

            val mediaInfo = CachedMediaInfo(
                title = currentDetail.title,
                coverImage = currentDetail.bannerImageUrl ?: currentDetail.coverImageUrl,
                totalEpisodes = currentDetail.episodes,
                totalChapters = currentDetail.chapters,
                totalVolumes = null
            )

            val result = if (currentDetail.isInList) {
                updateListEntryUseCase(
                    mediaId = currentDetail.id,
                    mediaType = mediaType,
                    status = status,
                    progress = progress,
                    score = score,
                    notes = note.takeIf { it.isNotBlank() },
                    startDate = startDate,
                    repeatCount = rewatches,
                    priority = priority
                )
            } else {
                addToListUseCase(
                    mediaId = currentDetail.id,
                    mediaType = mediaType,
                    status = status,
                    mediaInfo = mediaInfo
                ).also {
                    if (it is Result.Success) {
                        updateListEntryUseCase(
                            mediaId = currentDetail.id,
                            mediaType = mediaType,
                            progress = progress,
                            score = score,
                            notes = note.takeIf { it.isNotBlank() },
                            startDate = startDate,
                            repeatCount = rewatches,
                            priority = priority
                        )
                    }
                }
            }

            when (result) {
                is Result.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            mediaDetail = currentState.mediaDetail?.copy(
                                isInList = true
                            )
                        )
                    }
                }
                is Result.Failure -> {
                    // TODO: Handle error
                }
            }
        }
    }

    fun incrementProgress() {
        viewModelScope.launch {
            val currentDetail = _state.value.mediaDetail ?: return@launch
            val currentEntry = _state.value.listEntry ?: return@launch
            val mediaType = currentDetail.type.toCore()

            val total = when (mediaType) {
                com.yumedev.seijakulistkmp.core.domain.model.MediaType.ANIME -> currentDetail.episodes
                com.yumedev.seijakulistkmp.core.domain.model.MediaType.MANGA -> currentDetail.chapters
            }

            val newProgress = currentEntry.progress + 1
            val maxProgress = total ?: Int.MAX_VALUE

            if (newProgress > maxProgress) return@launch

            val newStatus = if (total != null && newProgress == total) {
                MediaListStatus.COMPLETED
            } else {
                currentEntry.status
            }

            updateListEntryUseCase(
                mediaId = currentDetail.id,
                mediaType = mediaType,
                progress = newProgress,
                status = newStatus
            )
        }
    }
}
