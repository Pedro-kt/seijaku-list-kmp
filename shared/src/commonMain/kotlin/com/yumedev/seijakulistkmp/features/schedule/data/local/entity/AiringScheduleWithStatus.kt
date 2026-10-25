package com.yumedev.seijakulistkmp.features.schedule.data.local.entity

import androidx.room3.ColumnInfo
import androidx.room3.Embedded

data class AiringScheduleWithStatus(
    @Embedded
    val schedule: AiringScheduleEntity,

    @ColumnInfo(name = "user_list_status")
    val userListStatus: String?
)
