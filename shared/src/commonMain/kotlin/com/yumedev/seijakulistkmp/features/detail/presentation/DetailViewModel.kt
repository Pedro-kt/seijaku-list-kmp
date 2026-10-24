package com.yumedev.seijakulistkmp.features.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.features.detail.domain.model.Episode
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetAnimeDetailUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetMangaDetailUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.ObserveFavoriteEpisodesByMediaUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.ToggleFavoriteEpisodeUseCase
import com.yumedev.seijakulistkmp.features.auth.domain.usecase.GetCurrentUserUseCase
import com.yumedev.seijakulistkmp.features.detail.presentation.utils.toCore
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.GetCurrentProfileUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.model.CachedMediaInfo
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ValidationError
import com.yumedev.seijakulistkmp.features.tracking.domain.validator.MediaListValidator
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.AddToListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.CheckInListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetListEntryUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.UpdateListEntryUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val getMangaDetailUseCase: GetMangaDetailUseCase,
    private val addToListUseCase: AddToListUseCase,
    private val updateListEntryUseCase: UpdateListEntryUseCase,
    private val checkInListUseCase: CheckInListUseCase,
    private val getListEntryUseCase: GetListEntryUseCase,
    private val toggleFavoriteEpisodeUseCase: ToggleFavoriteEpisodeUseCase,
    private val observeFavoriteEpisodesByMediaUseCase: ObserveFavoriteEpisodesByMediaUseCase,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val _events = Channel<DetailEvent>()
    val events: Flow<DetailEvent> = _events.receiveAsFlow()

    private var observeListEntryJob: Job? = null
    private var observeFavoriteEpisodesJob: Job? = null
    private var currentMediaId: Int? = null

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                val mediaId = currentMediaId
                if (mediaId != null) {
                    observeFavoriteEpisodes(mediaId)
                }
            }
        }
    }

    private suspend fun getUserId(): String {
        val firebaseUser = getCurrentUserUseCase().first()
        return if (firebaseUser != null) {
            firebaseUser.uid
        } else {
            val profile = getCurrentProfileUseCase().first()
            profile?.id?.toString() ?: "1"
        }
    }

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

                if (isInList) {
                    val entryResult = getListEntryUseCase(id, type.toCore())
                    entryResult.onSuccess { existingEntry ->
                        if (existingEntry != null && existingEntry.mediaStatus != mediaDetail.status) {
                            updateListEntryUseCase(
                                mediaId = id,
                                mediaType = type.toCore(),
                                mediaStatus = mediaDetail.status
                            )
                        }
                    }
                }

                observeListEntry(id, type)
                observeFavoriteEpisodes(id)
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

    private fun observeFavoriteEpisodes(mediaId: Int) {
        currentMediaId = mediaId
        observeFavoriteEpisodesJob?.cancel()
        viewModelScope.launch {
            val userId = getUserId()
            observeFavoriteEpisodesJob = observeFavoriteEpisodesByMediaUseCase(
                userId = userId,
                mediaId = mediaId
            ).onEach { favoriteEpisodes ->
                _state.update { currentState ->
                    val updatedMediaDetail = currentState.mediaDetail?.let { detail ->
                        val updatedEpisodes = detail.episodes_list?.map { episode ->
                            episode.copy(
                                isFavorite = favoriteEpisodes.any { it.episodeNumber == episode.number }
                            )
                        }
                        detail.copy(episodes_list = updatedEpisodes)
                    }
                    currentState.copy(mediaDetail = updatedMediaDetail)
                }
            }.launchIn(viewModelScope)
        }
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

    fun toggleEpisodeFavorite(episodeNumber: Int, episodeTitle: String, thumbnailUrl: String?) {
        viewModelScope.launch {
            val currentDetail = _state.value.mediaDetail ?: return@launch
            val userId = getUserId()

            toggleFavoriteEpisodeUseCase(
                userId = userId,
                mediaId = currentDetail.id,
                episodeNumber = episodeNumber,
                episodeTitle = episodeTitle,
                thumbnailUrl = thumbnailUrl
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

            val currentListEntry = _state.value.listEntry
            val isChangingStatus = currentListEntry != null && currentListEntry.status != status

            if (isChangingStatus && !MediaListValidator.isStatusAllowed(status, currentDetail.status)) {
                _events.send(DetailEvent.ShowError("Cannot change to this status while media is ${currentDetail.status}"))
                return@launch
            }

            val total = when (mediaType) {
                com.yumedev.seijakulistkmp.core.domain.model.MediaType.ANIME -> currentDetail.episodes
                com.yumedev.seijakulistkmp.core.domain.model.MediaType.MANGA -> currentDetail.chapters
            }

            val validation = MediaListValidator.validateStatusProgressConsistency(
                newStatus = status,
                newProgress = progress,
                total = total,
                mediaStatus = currentDetail.status
            )
            if (validation is MediaListValidator.ValidationResult.Invalid) {
                val errorMessage = when (val error = validation.error) {
                    is ValidationError.ProgressNotZeroForPlanning -> "Progress must be 0 when status is Planning"
                    is ValidationError.ProgressNotCompleteForCompleted -> "Progress must be complete when status is Completed"
                    is ValidationError.ProgressNegative -> "Progress cannot be negative"
                    is ValidationError.ProgressExceedsMaxAllowed -> "Progress exceeds max allowed: ${error.maxAllowed}"
                    is ValidationError.ProgressExceedsTotal -> "Progress exceeds total episodes/chapters"
                }
                _events.send(DetailEvent.ShowError(errorMessage))
                return@launch
            }

            if (!MediaListValidator.isScoreValid(score)) {
                _events.send(DetailEvent.ShowError("Score must be between 0 and 100"))
                return@launch
            }

            if (!MediaListValidator.isNoteLengthValid(note)) {
                _events.send(DetailEvent.ShowError("Note is too long (max 2000 characters)"))
                return@launch
            }

            val mediaInfo = CachedMediaInfo(
                title = currentDetail.title,
                coverImage = currentDetail.coverImageUrl ?: currentDetail.bannerImageUrl,
                totalEpisodes = currentDetail.episodes,
                totalChapters = currentDetail.chapters,
                totalVolumes = null,
                mediaStatus = currentDetail.status
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
                    _events.send(DetailEvent.ShowError("Failed to save to list: ${result.exception.message}"))
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

            if (!MediaListValidator.canIncrementProgress(
                currentStatus = currentEntry.status,
                currentProgress = currentEntry.progress,
                total = total,
                mediaStatus = currentDetail.status
            )) {
                _events.send(DetailEvent.ShowError("Cannot increment progress with current status"))
                return@launch
            }

            val newProgress = currentEntry.progress + 1

            val newStatus = if (total != null && newProgress == total && currentDetail.status?.uppercase() == "FINISHED") {
                MediaListValidator.getAutoStatusOnCompletion(currentEntry.status, currentDetail.status)
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
