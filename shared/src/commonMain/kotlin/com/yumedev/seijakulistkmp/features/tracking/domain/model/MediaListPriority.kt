package com.yumedev.seijakulistkmp.features.tracking.domain.model

enum class MediaListPriority(val value: Int) {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    companion object {
        fun fromValue(value: Int): MediaListPriority {
            return entries.find { it.value == value } ?: MEDIUM
        }
    }
}
