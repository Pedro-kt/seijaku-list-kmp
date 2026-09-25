package com.yumedev.seijakulistkmp.features.schedule.domain.model

import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
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
