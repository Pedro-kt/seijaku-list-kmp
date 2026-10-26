package com.yumedev.seijakulistkmp.core.analytics

class JvmAnalyticsManager : AnalyticsManager {
    override fun logEvent(eventName: String, params: Map<String, Any>) {
    }

    override fun setUserProperty(name: String, value: String) {
    }

    override fun setUserId(userId: String?) {
    }

    override fun logScreenView(screenName: String, screenClass: String) {
    }

    override fun logError(throwable: Throwable, context: Map<String, String>) {
    }

    override fun setCustomKey(key: String, value: String) {
    }

    override fun setCustomKey(key: String, value: Boolean) {
    }

    override fun setCustomKey(key: String, value: Int) {
    }
}
