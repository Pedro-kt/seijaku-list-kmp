package com.yumedev.seijakulistkmp.features.schedule.domain.model

data class TimeSlot(
    val hour: Int,
    val minute: Int,
    val items: List<AiringScheduleItem>
) {
    val displayTime: String
        get() = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}
