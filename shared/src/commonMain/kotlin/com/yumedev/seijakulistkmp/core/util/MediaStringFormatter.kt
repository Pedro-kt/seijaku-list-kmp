package com.yumedev.seijakulistkmp.core.util

interface MediaStringFormatter {
    suspend fun formatMediaFormat(format: String?): String?
    suspend fun formatEpisodes(episodes: Int?): String?
}
