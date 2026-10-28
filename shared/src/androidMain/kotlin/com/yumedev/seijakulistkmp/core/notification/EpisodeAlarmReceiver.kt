package com.yumedev.seijakulistkmp.core.notification

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

class EpisodeAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val mediaId = intent.getIntExtra(EXTRA_MEDIA_ID, -1)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val episode = intent.getIntExtra(EXTRA_EPISODE, 0)
        val airingAt = intent.getLongExtra(EXTRA_AIRING_AT, 0)

        if (mediaId == -1 || title == null || airingAt == 0L) {
            return
        }

        if (!hasNotificationPermission(context)) {
            return
        }

        showNotification(context, mediaId, title, episode, airingAt)
    }

    private fun hasNotificationPermission(context: Context): Boolean {
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
        context: Context,
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

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_MEDIA_ID, mediaId)
        }

        val pendingIntent = launchIntent?.let {
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
        private const val CHANNEL_ID = "airing_notifications"

        const val EXTRA_MEDIA_ID = "extra_media_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_EPISODE = "extra_episode"
        const val EXTRA_AIRING_AT = "extra_airing_at"
    }
}
