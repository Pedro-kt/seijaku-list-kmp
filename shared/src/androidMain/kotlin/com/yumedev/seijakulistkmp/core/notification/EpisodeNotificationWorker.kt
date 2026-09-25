package com.yumedev.seijakulistkmp.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

class EpisodeNotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val mediaId = inputData.getInt(ScheduleSyncWorker.KEY_MEDIA_ID, -1)
            val title = inputData.getString(ScheduleSyncWorker.KEY_TITLE) ?: return Result.failure()
            val episode = inputData.getInt(ScheduleSyncWorker.KEY_EPISODE, 0)
            val airingAt = inputData.getLong(ScheduleSyncWorker.KEY_AIRING_AT, 0)

            if (mediaId == -1 || airingAt == 0L) {
                return Result.failure()
            }

            createNotificationChannel()
            showNotification(mediaId, title, episode, airingAt)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Airing Notifications"
            val descriptionText = "Notifications for anime episodes airing soon"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(
        mediaId: Int,
        title: String,
        episode: Int,
        airingAt: Long
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val timeZone = TimeZone.currentSystemDefault()

        val airingTime = AiringScheduleTimeUtil.formatAiringTime(
            timestamp = airingAt,
            timeZone = timeZone,
            use24Hour = false
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // TODO: Use app icon
            .setContentTitle("$title - Episode $episode")
            .setContentText("Airing in 1 hour at $airingTime")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(mediaId, notification)
    }

    companion object {
        const val CHANNEL_ID = "airing_notifications"
    }
}
