package com.yumedev.seijakulistkmp.core.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class AndroidAiringNotificationScheduler(
    private val context: Context
) : AiringNotificationScheduler {

    private val workManager = WorkManager.getInstance(context)

    override fun startPeriodicSync() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<ScheduleSyncWorker>(
            repeatInterval = 6,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).build()

        workManager.enqueueUniquePeriodicWork(
            ScheduleSyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            syncWorkRequest
        )
    }

    override fun cancelAll() {
        workManager.cancelUniqueWork(ScheduleSyncWorker.WORK_NAME)

        workManager.cancelAllWorkByTag(ScheduleSyncWorker.TAG_EPISODE_NOTIFICATION)
    }
}
