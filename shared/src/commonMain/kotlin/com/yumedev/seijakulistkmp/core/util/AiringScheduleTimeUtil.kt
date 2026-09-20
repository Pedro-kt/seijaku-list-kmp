package com.yumedev.seijakulistkmp.core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object AiringScheduleTimeUtil {

    fun getCurrentWeekStart(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        val now = Clock.System.now()
        val localDate = now.toLocalDateTime(timeZone).date
        val dayOfWeek = localDate.dayOfWeek
        val daysToSubtract = dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
        val weekStart = localDate.minus(daysToSubtract, DateTimeUnit.DAY)
        return weekStart.atStartOfDayIn(timeZone).epochSeconds
    }

    fun getWeekRange(timeZone: TimeZone = TimeZone.currentSystemDefault()): Pair<Long, Long> {
        val now = Clock.System.now()
        val localDate = now.toLocalDateTime(timeZone).date
        val dayOfWeek = localDate.dayOfWeek

        val daysToSubtract = dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
        val weekStart = localDate.minus(daysToSubtract, DateTimeUnit.DAY)
        val weekEnd = weekStart.plus(7, DateTimeUnit.DAY)

        val startInstant = weekStart.atStartOfDayIn(timeZone)
        val endInstant = weekEnd.atStartOfDayIn(timeZone)

        return Pair(startInstant.epochSeconds, endInstant.epochSeconds)
    }

    fun convertTimestampToLocalDateTime(timestamp: Long, timeZone: TimeZone): LocalDateTime {
        val instant = Instant.fromEpochSeconds(timestamp)
        return instant.toLocalDateTime(timeZone)
    }

    fun formatAiringTime(timestamp: Long, timeZone: TimeZone, use24Hour: Boolean = false): String {
        val localDateTime = convertTimestampToLocalDateTime(timestamp, timeZone)
        val hour = localDateTime.hour
        val minute = localDateTime.minute.toString().padStart(2, '0')

        return if (use24Hour) {
            "${hour.toString().padStart(2, '0')}:$minute"
        } else {
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            val period = if (hour < 12) "AM" else "PM"
            "$displayHour:$minute $period"
        }
    }

    fun formatCountdown(timeUntilAiring: Long): String {
        if (timeUntilAiring <= 0) return "Airing now"

        val seconds = timeUntilAiring
        val days = seconds / 86400
        val hours = (seconds % 86400) / 3600
        val minutes = (seconds % 3600) / 60

        return when {
            days > 0 -> "${days}d ${hours}h"
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "Less than 1m"
        }
    }

    fun getWeekDays(timeZone: TimeZone = TimeZone.currentSystemDefault()): List<LocalDate> {
        val now = Clock.System.now()
        val localDate = now.toLocalDateTime(timeZone).date
        val dayOfWeek = localDate.dayOfWeek

        val daysToSubtract = dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
        val weekStart = localDate.minus(daysToSubtract, DateTimeUnit.DAY)

        return (0..6).map { offset ->
            weekStart.plus(offset, DateTimeUnit.DAY)
        }
    }

    fun isToday(date: LocalDate, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        val now = Clock.System.now()
        val today = now.toLocalDateTime(timeZone).date
        return date == today
    }

    fun getDayRange(date: LocalDate, timeZone: TimeZone): Pair<Long, Long> {
        val dayStart = date.atStartOfDayIn(timeZone)
        val dayEnd = date.plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)
        return Pair(dayStart.epochSeconds, dayEnd.epochSeconds)
    }

    fun calculateTimeUntilAiring(airingAt: Long): Long {
        val now = Clock.System.now().epochSeconds
        return airingAt - now
    }
}
