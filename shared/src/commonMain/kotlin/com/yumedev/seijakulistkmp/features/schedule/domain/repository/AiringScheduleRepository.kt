package com.yumedev.seijakulistkmp.features.schedule.domain.repository

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import kotlinx.coroutines.flow.Flow

interface AiringScheduleRepository {

    suspend fun getWeeklySchedule(forceRefresh: Boolean = false): Result<List<AiringScheduleItem>>

    fun observeWeeklySchedule(): Flow<List<AiringScheduleItem>>

    suspend fun clearCache()

    suspend fun isCacheValid(): Boolean
}
