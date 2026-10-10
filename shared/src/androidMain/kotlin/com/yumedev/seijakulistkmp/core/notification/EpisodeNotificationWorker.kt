package com.yumedev.seijakulistkmp.core.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
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

            showNotification(mediaId, title, episode, airingAt)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
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
        val notificationIcon = if (iconResId != 0) iconResId else android.R.drawable.ic_dialog_info

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(notificationIcon)
            .setContentTitle("$title - Episode $episode")
            .setContentText("Airing in 1 hour at $airingTime")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(mediaId, notification)
    }

    companion object {
        const val CHANNEL_ID = "airing_notifications"
        const val EXTRA_MEDIA_ID = "extra_media_id"
    }
}
