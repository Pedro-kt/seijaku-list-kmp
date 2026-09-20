package com.yumedev.seijakulistkmp.features.schedule.domain.model

import kotlinx.datetime.TimeZone

enum class TimezonePreference(
    val displayName: String,
    val timeZone: TimeZone
) {
    JST(
        displayName = "Japan (JST)",
        timeZone = TimeZone.of("Asia/Tokyo")
    ),
    SYSTEM(
        displayName = "System Default",
        timeZone = TimeZone.currentSystemDefault()
    );

    companion object {
        fun fromTimeZone(timeZone: TimeZone): TimezonePreference {
            return entries.find { it.timeZone == timeZone } ?: SYSTEM
        }
    }
}
