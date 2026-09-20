package com.yumedev.seijakulistkmp.features.schedule.domain.usecase

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import com.yumedev.seijakulistkmp.features.schedule.domain.repository.AiringScheduleRepository
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours

class GetUserUpcomingEpisodesUseCase(
    private val airingScheduleRepository: AiringScheduleRepository,
    private val mediaListRepository: MediaListRepository
) {
    operator fun invoke(): Flow<List<AiringScheduleItem>> {
        val now = Clock.System.now()
        val next48Hours = now + 48.hours

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
                val isUpcoming = airingTime >= now && airingTime <= next48Hours

                airingItem.mediaId in userAnimeIds && isUpcoming
            }
        }
    }
}
