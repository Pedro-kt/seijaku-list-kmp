package com.yumedev.seijakulistkmp.features.tracking.domain.model

data class MediaListStats(
    val totalEntries: Int,
    val currentCount: Int,
    val completedCount: Int,
    val planningCount: Int,
    val droppedCount: Int,
    val pausedCount: Int,
    val repeatingCount: Int,
    val totalProgress: Int,
    val averageScore: Float?
)
