package com.yumedev.seijakulistkmp.features.schedule.presentation

import com.yumedev.seijakulistkmp.features.schedule.domain.model.ScheduleDay
import com.yumedev.seijakulistkmp.features.schedule.domain.model.TimezonePreference
import kotlinx.datetime.DayOfWeek

data class AiringScheduleState(
    val scheduleDays: List<ScheduleDay> = emptyList(),
    val selectedDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val selectedTimezone: TimezonePreference = TimezonePreference.SYSTEM,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)
