package com.yumedev.seijakulistkmp.features.schedule.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import com.yumedev.seijakulistkmp.features.schedule.domain.model.ScheduleDay
import com.yumedev.seijakulistkmp.features.schedule.domain.repository.AiringScheduleRepository
import kotlinx.datetime.TimeZone

class GetWeeklyScheduleUseCase(
    private val repository: AiringScheduleRepository
) {
    suspend operator fun invoke(
        forceRefresh: Boolean = false,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Result<List<ScheduleDay>> {
        return repository.getWeeklySchedule(forceRefresh).map { scheduleItems ->
            groupScheduleByDays(scheduleItems, timeZone)
        }
    }

    private fun groupScheduleByDays(
        scheduleItems: List<AiringScheduleItem>,
        timeZone: TimeZone
    ): List<ScheduleDay> {
        val weekDays = AiringScheduleTimeUtil.getWeekDays(timeZone)

        return weekDays.map { date ->
            val (dayStart, dayEnd) = AiringScheduleTimeUtil.getDayRange(date, timeZone)

            val itemsForDay = scheduleItems.filter { item ->
                item.airingAt >= dayStart && item.airingAt < dayEnd
            }.sortedBy { it.airingAt }

            ScheduleDay(
                dayOfWeek = date.dayOfWeek,
                date = date,
                airingItems = itemsForDay,
                isToday = AiringScheduleTimeUtil.isToday(date, timeZone)
            )
        }
    }
}
