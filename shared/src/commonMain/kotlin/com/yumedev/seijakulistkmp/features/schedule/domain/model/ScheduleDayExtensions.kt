package com.yumedev.seijakulistkmp.features.schedule.domain.model

import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import com.yumedev.seijakulistkmp.features.schedule.presentation.ScheduleFilter
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import kotlinx.datetime.TimeZone

fun List<AiringScheduleItem>.groupByTimeSlots(
    timeZone: TimeZone,
    slotIntervalMinutes: Int = 30
): List<TimeSlot> {
    return this
        .groupBy { item ->
            val dateTime = AiringScheduleTimeUtil.convertTimestampToLocalDateTime(
                item.airingAt,
                timeZone
            )
            val roundedMinute = (dateTime.minute / slotIntervalMinutes) * slotIntervalMinutes
            Pair(dateTime.hour, roundedMinute)
        }
        .map { (time, items) ->
            TimeSlot(
                hour = time.first,
                minute = time.second,
                items = items.sortedBy { it.airingAt }
            )
        }
        .sortedBy { it.hour * 60 + it.minute }
}

fun List<ScheduleDay>.filterByScheduleFilter(filter: ScheduleFilter): List<ScheduleDay> {
    return when (filter) {
        ScheduleFilter.ALL -> this
        ScheduleFilter.IN_MY_LIST -> this.map { day ->
            day.copy(
                airingItems = day.airingItems.filter { item ->
                    item.userListStatus != null
                }
            )
        }
        ScheduleFilter.PLAN_TO_WATCH -> this.map { day ->
            day.copy(
                airingItems = day.airingItems.filter { item ->
                    item.userListStatus == MediaListStatus.PLANNING
                }
            )
        }
    }
}
