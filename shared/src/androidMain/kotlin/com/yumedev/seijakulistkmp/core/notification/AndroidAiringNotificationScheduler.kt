package com.yumedev.seijakulistkmp.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import com.yumedev.seijakulistkmp.features.schedule.domain.usecase.GetUserUpcomingEpisodesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours

class AndroidAiringNotificationScheduler(
    private val context: Context
) : AiringNotificationScheduler, KoinComponent {

    private val workManager = WorkManager.getInstance(context)
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val getUserUpcomingEpisodesUseCase: GetUserUpcomingEpisodesUseCase by inject()

    override fun startPeriodicSync() {
        startPeriodicWorkManagerSync()
        scheduleNotificationsWithAlarmManager()
    }

    private fun startPeriodicWorkManagerSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<ScheduleSyncWorker>(
            repeatInterval = 6,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            ScheduleSyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            syncWorkRequest
        )
    }

    private fun scheduleNotificationsWithAlarmManager() {
        scope.launch {
            try {
                val now = Clock.System.now()
                val upcomingEpisodes = getUserUpcomingEpisodesUseCase().first()

                upcomingEpisodes.forEach { episode ->
                    scheduleAlarm(episode, now)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

    override fun cancelAll() {
        workManager.cancelUniqueWork(ScheduleSyncWorker.WORK_NAME)

        scope.launch {
            try {
                val upcomingEpisodes = getUserUpcomingEpisodesUseCase().first()
                upcomingEpisodes.forEach { episode ->
                    cancelAlarm(episode.mediaId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun cancelAlarm(mediaId: Int) {
        val intent = Intent(context, EpisodeAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            mediaId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
