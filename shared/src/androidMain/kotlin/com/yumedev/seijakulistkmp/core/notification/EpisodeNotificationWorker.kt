package com.yumedev.seijakulistkmp.core.notification

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

class EpisodeNotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val mediaId = inputData.getInt("media_id", -1)
            val title = inputData.getString("title")
            val episode = inputData.getInt("episode", 0)
            val airingAt = inputData.getLong("airing_at", 0)

            if (mediaId == -1 || title == null || airingAt == 0L) {
                return Result.failure()
            }

            if (!hasNotificationPermission()) {
                return Result.failure()
            }

            showNotification(mediaId, title, episode, airingAt)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
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

        val now = Clock.System.now()
        val airingTime = Instant.fromEpochSeconds(airingAt)
        val timeUntilAiring = (airingTime - now).inWholeMinutes

        val timeText = when {
            timeUntilAiring <= 0 -> "airing now!"
            timeUntilAiring < 60 -> "airing in $timeUntilAiring minutes"
            else -> {
                val formattedTime = AiringScheduleTimeUtil.formatAiringTime(
                    timestamp = airingAt,
                    timeZone = timeZone,
                    use24Hour = false
                )
                "airing at $formattedTime"
            }
        }

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_MEDIA_ID, mediaId)
        }

        val pendingIntent = intent?.let {
            PendingIntent.getActivity(
                context,
                mediaId,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val iconResId = context.resources.getIdentifier("ic_notification", "drawable", context.packageName)
        val notificationIcon = if (iconResId != 0) {
            iconResId
        } else {
            context.applicationInfo.icon
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(notificationIcon)
            .setContentTitle("$title - Episode $episode")
            .setContentText(timeText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(mediaId, notification)
    }

    companion object {
        const val CHANNEL_ID = "airing_notifications"
        const val EXTRA_MEDIA_ID = "extra_media_id"
    }
}
