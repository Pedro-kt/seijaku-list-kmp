package com.yumedev.seijakulistkmp.features.schedule.domain.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

data class ScheduleDay(
    val dayOfWeek: DayOfWeek,
    val date: LocalDate,
    val airingItems: List<AiringScheduleItem>,
    val isToday: Boolean = false
)
