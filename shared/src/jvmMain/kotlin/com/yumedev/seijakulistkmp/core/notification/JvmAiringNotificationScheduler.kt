package com.yumedev.seijakulistkmp.core.notification

class JvmAiringNotificationScheduler : AiringNotificationScheduler {
    override fun startPeriodicSync() {
        // No-op for desktop
    }

    override fun cancelAll() {
        // No-op for desktop
    }
}
