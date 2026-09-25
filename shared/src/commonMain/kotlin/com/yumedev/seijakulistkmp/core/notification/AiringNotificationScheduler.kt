package com.yumedev.seijakulistkmp.core.notification

interface AiringNotificationScheduler {
    fun startPeriodicSync()

    fun cancelAll()
}
