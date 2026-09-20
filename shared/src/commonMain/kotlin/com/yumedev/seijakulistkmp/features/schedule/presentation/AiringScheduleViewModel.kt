package com.yumedev.seijakulistkmp.features.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.error.ErrorMapper
import com.yumedev.seijakulistkmp.features.schedule.domain.model.TimezonePreference
import com.yumedev.seijakulistkmp.features.schedule.domain.usecase.GetWeeklyScheduleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AiringScheduleViewModel(
    private val getWeeklyScheduleUseCase: GetWeeklyScheduleUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _state = MutableStateFlow(AiringScheduleState())
    val state: StateFlow<AiringScheduleState> = _state.asStateFlow()

    init {
        val currentDayOfWeek = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .dayOfWeek
        _state.update { it.copy(selectedDayOfWeek = currentDayOfWeek) }
        loadWeeklySchedule()
    }

    fun loadWeeklySchedule(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = !forceRefresh, isRefreshing = forceRefresh, error = null) }

            getWeeklyScheduleUseCase(
                forceRefresh = forceRefresh,
                timeZone = _state.value.selectedTimezone.timeZone
            ).onSuccess { scheduleDays ->
                _state.update {
                    it.copy(
                        scheduleDays = scheduleDays,
                        isLoading = false,
                        isRefreshing = false,
                        error = null
                    )
                }
            }.onFailure { error ->
                val errorType = errorMapper.mapToErrorType(error)
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = errorType.toString()
                    )
                }
            }
        }
    }

    fun onDaySelected(dayOfWeek: DayOfWeek) {
        _state.update { it.copy(selectedDayOfWeek = dayOfWeek) }
    }

    fun onTimezoneSelected(timezone: TimezonePreference) {
        _state.update { it.copy(selectedTimezone = timezone) }
        loadWeeklySchedule(forceRefresh = true)
    }

    fun onRefresh() {
        loadWeeklySchedule(forceRefresh = true)
    }

    fun onErrorDismissed() {
        _state.update { it.copy(error = null) }
    }
}
