package com.yumedev.seijakulistkmp.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import com.yumedev.seijakulistkmp.features.schedule.domain.usecase.GetUserUpcomingEpisodesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.hours

class ScheduleSyncWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val getUserUpcomingEpisodesUseCase: GetUserUpcomingEpisodesUseCase by inject()
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun doWork(): Result {
        return try {
            val now = Clock.System.now()
            val airingAnime = getUserUpcomingEpisodesUseCase().first()

            airingAnime.forEach { anime ->
                scheduleAlarm(anime, now)
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun scheduleAlarm(episode: AiringScheduleItem, now: Instant) {
        val airingTime = Instant.fromEpochSeconds(episode.airingAt)
        val notificationTime = airingTime - 1.hours

        if (notificationTime <= now) {
            return
        }

        val intent = Intent(context, EpisodeAlarmReceiver::class.java).apply {
            putExtra(EpisodeAlarmReceiver.EXTRA_MEDIA_ID, episode.mediaId)
            putExtra(EpisodeAlarmReceiver.EXTRA_TITLE, episode.mediaTitle)
            putExtra(EpisodeAlarmReceiver.EXTRA_EPISODE, episode.episode)
            putExtra(EpisodeAlarmReceiver.EXTRA_AIRING_AT, episode.airingAt)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            episode.mediaId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = notificationTime.toEpochMilliseconds()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val WORK_NAME = "schedule_sync"
    }
}
