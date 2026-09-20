package com.yumedev.seijakulistkmp.features.schedule.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import com.yumedev.seijakulistkmp.features.schedule.domain.repository.AiringScheduleRepository
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class GetUserAiringAnimeTodayUseCase(
    private val airingScheduleRepository: AiringScheduleRepository,
    private val mediaListRepository: MediaListRepository
) {
    operator fun invoke(timeZone: TimeZone): Flow<List<AiringScheduleItem>> {
        val now = Clock.System.now()
        val localDate = now.toLocalDateTime(timeZone).date
        val startOfDay = Instant.parse("${localDate}T00:00:00Z")
        val endOfDay = Instant.parse("${localDate}T23:59:59Z")

        return combine(
            airingScheduleRepository.observeWeeklySchedule(),
            mediaListRepository.observeList(
                mediaType = MediaType.ANIME,
                status = MediaListStatus.CURRENT
            )
        ) { weeklySchedule, userAnimeList ->
            val userAnimeIds = userAnimeList.map { it.mediaId }.toSet()

            weeklySchedule.filter { airingItem ->
                val airingTime = Instant.fromEpochSeconds(airingItem.airingAt)
                val isToday = airingTime >= startOfDay && airingTime <= endOfDay

                airingItem.mediaId in userAnimeIds && isToday
            }
        }
    }
}
