package com.yumedev.seijakulistkmp.core.analytics

interface AnalyticsManager {
    fun logEvent(eventName: String, params: Map<String, Any> = emptyMap())

    fun setUserProperty(name: String, value: String)

    fun setUserId(userId: String?)

    fun logScreenView(screenName: String, screenClass: String)

    fun logError(throwable: Throwable, context: Map<String, String> = emptyMap())

    fun setCustomKey(key: String, value: String)

    fun setCustomKey(key: String, value: Boolean)

    fun setCustomKey(key: String, value: Int)
}

object AnalyticsEvents {
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"
    const val LOGOUT = "logout"

    const val ADD_TO_LIST = "add_to_list"
    const val UPDATE_PROGRESS = "update_progress"
    const val CHANGE_STATUS = "change_status"
    const val REMOVE_FROM_LIST = "remove_from_list"

    const val SEARCH = "search"
    const val VIEW_DETAIL = "view_detail"
    const val VIEW_CHARACTER = "view_character"

    const val EXPORT_LIST = "export_list"
    const val IMPORT_LIST = "import_list"

    const val ENABLE_NOTIFICATIONS = "enable_notifications"
    const val DISABLE_NOTIFICATIONS = "disable_notifications"

    const val SYNC_DATA = "sync_data"
    const val SYNC_ERROR = "sync_error"

    const val THEME_CHANGED = "theme_changed"
    const val LANGUAGE_CHANGED = "language_changed"
}

object AnalyticsParams {
    const val MEDIA_TYPE = "media_type"
    const val MEDIA_ID = "media_id"
    const val MEDIA_TITLE = "media_title"
    const val STATUS = "status"
    const val PROGRESS = "progress"
    const val SEARCH_QUERY = "search_query"
    const val LIST_TYPE = "list_type"
    const val THEME = "theme"
    const val LANGUAGE = "language"
    const val ERROR_MESSAGE = "error_message"
    const val SCREEN_NAME = "screen_name"
    const val IMPORT_COUNT = "import_count"
    const val EXPORT_COUNT = "export_count"
}

object AnalyticsUserProperties {
    const val NOTIFICATIONS_ENABLED = "notifications_enabled"
    const val THEME_MODE = "theme_mode"
    const val LANGUAGE = "language"
    const val SFW_MODE = "sfw_mode"
    const val TOTAL_ANIME_COUNT = "total_anime_count"
    const val TOTAL_MANGA_COUNT = "total_manga_count"
}
