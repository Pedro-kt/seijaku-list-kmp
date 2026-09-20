package com.yumedev.seijakulistkmp.features.schedule.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.core.domain.model.resultOf
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import com.yumedev.seijakulistkmp.data.remote.graphql.GetWeeklyAiringScheduleQuery
import kotlinx.datetime.TimeZone

interface AiringScheduleDataSource {
    suspend fun fetchWeeklySchedule(): Result<List<GetWeeklyAiringScheduleQuery.AiringSchedule>>
}

class AiringScheduleDataSourceImpl(
    private val apolloClient: ApolloClient
) : AiringScheduleDataSource {

    override suspend fun fetchWeeklySchedule(): Result<List<GetWeeklyAiringScheduleQuery.AiringSchedule>> = resultOf {
        val (weekStart, weekEnd) = AiringScheduleTimeUtil.getWeekRange(TimeZone.currentSystemDefault())

        val allSchedules = mutableListOf<GetWeeklyAiringScheduleQuery.AiringSchedule>()
        var currentPage = 1
        var hasNextPage = true

        while (hasNextPage) {
            val response = apolloClient.query(
                GetWeeklyAiringScheduleQuery(
                    airingAtGreater = weekStart.toInt(),
                    airingAtLesser = weekEnd.toInt(),
                    page = Optional.present(currentPage),
                    perPage = Optional.present(25)
                )
            ).execute()

            if (response.hasErrors()) {
                throw Exception(response.errors?.firstOrNull()?.message ?: "Unknown GraphQL error")
            }

            val page = response.data?.Page ?: throw Exception("No data returned from API")
            val schedules = page.airingSchedules?.filterNotNull() ?: emptyList()

            allSchedules.addAll(schedules)

            hasNextPage = page.pageInfo?.hasNextPage ?: false
            currentPage++

            if (currentPage > 10) {
                break
            }
        }

        allSchedules
    }
}
