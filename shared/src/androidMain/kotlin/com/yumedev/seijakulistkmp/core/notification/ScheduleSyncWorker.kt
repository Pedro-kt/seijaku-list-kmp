package com.yumedev.seijakulistkmp.core.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.yumedev.seijakulistkmp.features.schedule.domain.usecase.GetUserUpcomingEpisodesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class ScheduleSyncWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val getUserUpcomingEpisodesUseCase: GetUserUpcomingEpisodesUseCase by inject()
    private val workManager = WorkManager.getInstance(context)

    override suspend fun doWork(): Result {
        return try {
            val now = Clock.System.now()

            val airingAnime = getUserUpcomingEpisodesUseCase().first()

            cancelOldNotifications()

            airingAnime.forEach { anime ->
                val airingTime = Instant.fromEpochSeconds(anime.airingAt)
                val notificationTime = airingTime - 1.hours

                if (notificationTime > now) {
                    val delay = (notificationTime - now).inWholeMilliseconds

                    val inputData = Data.Builder()
                        .putInt(KEY_MEDIA_ID, anime.mediaId)
                        .putString(KEY_TITLE, anime.mediaTitle)
                        .putInt(KEY_EPISODE, anime.episode)
                        .putLong(KEY_AIRING_AT, anime.airingAt)
                        .build()

                    val notificationWork = OneTimeWorkRequestBuilder<EpisodeNotificationWorker>()
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInputData(inputData)
                        .addTag(TAG_EPISODE_NOTIFICATION)
                        .addTag("episode_${anime.id}")
                        .build()

                    workManager.enqueue(notificationWork)
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun cancelOldNotifications() {
        workManager.cancelAllWorkByTag(TAG_EPISODE_NOTIFICATION)
    }

    companion object {
        const val WORK_NAME = "schedule_sync"
        const val TAG_EPISODE_NOTIFICATION = "episode_notification"

        const val KEY_MEDIA_ID = "media_id"
        const val KEY_TITLE = "title"
        const val KEY_EPISODE = "episode"
        const val KEY_AIRING_AT = "airing_at"
    }
}
