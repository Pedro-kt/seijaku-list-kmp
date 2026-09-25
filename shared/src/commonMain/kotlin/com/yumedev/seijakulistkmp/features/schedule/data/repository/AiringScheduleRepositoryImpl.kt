package com.yumedev.seijakulistkmp.features.schedule.data.repository

import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.core.domain.model.resultOf
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import com.yumedev.seijakulistkmp.features.schedule.data.datasource.AiringScheduleDataSource
import com.yumedev.seijakulistkmp.features.schedule.data.local.dao.AiringScheduleDao
import com.yumedev.seijakulistkmp.features.schedule.data.mapper.toAiringScheduleEntities
import com.yumedev.seijakulistkmp.features.schedule.data.mapper.toAiringScheduleItems
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import com.yumedev.seijakulistkmp.features.schedule.domain.repository.AiringScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone

class AiringScheduleRepositoryImpl(
    private val dataSource: AiringScheduleDataSource,
    private val dao: AiringScheduleDao
) : AiringScheduleRepository {

    override suspend fun getWeeklySchedule(forceRefresh: Boolean): Result<List<AiringScheduleItem>> = resultOf {
        val weekStart = AiringScheduleTimeUtil.getCurrentWeekStart()

        if (!forceRefresh && isCacheValid()) {
            val cached = dao.getScheduleForWeek(weekStart)
            if (cached.isNotEmpty()) {
                return@resultOf cached.toAiringScheduleItems()
            }
        }

        val schedules = dataSource.fetchWeeklySchedule().getOrThrow()
        val now = Clock.System.now().epochSeconds

        val entities = schedules.toAiringScheduleEntities(weekStart, now)

        dao.deleteWeekSchedules(weekStart)
        dao.insertSchedules(entities)

        entities.toAiringScheduleItems()
    }

    override fun observeWeeklySchedule(): Flow<List<AiringScheduleItem>> {
        val weekStart = AiringScheduleTimeUtil.getCurrentWeekStart()
        return dao.observeScheduleForWeek(weekStart)
            .map { it.toAiringScheduleItems() }
    }

    override suspend fun clearCache() {
        dao.deleteAllSchedules()
    }

    override suspend fun isCacheValid(): Boolean {
        val weekStart = AiringScheduleTimeUtil.getCurrentWeekStart()
        val count = dao.getScheduleCountForWeek(weekStart)
        return count > 0
    }
}
