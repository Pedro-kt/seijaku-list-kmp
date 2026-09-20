package com.yumedev.seijakulistkmp.features.schedule.domain.usecase

import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import com.yumedev.seijakulistkmp.features.schedule.domain.repository.AiringScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

class GetDayScheduleUseCase(
    private val repository: AiringScheduleRepository
) {
    operator fun invoke(
        date: LocalDate,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Flow<List<AiringScheduleItem>> {
        val (dayStart, dayEnd) = AiringScheduleTimeUtil.getDayRange(date, timeZone)

        return repository.observeWeeklySchedule().map { scheduleItems ->
            scheduleItems
                .filter { it.airingAt >= dayStart && it.airingAt < dayEnd }
                .sortedBy { it.airingAt }
        }
    }
}
