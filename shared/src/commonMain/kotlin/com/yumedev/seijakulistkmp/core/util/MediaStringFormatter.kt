package com.yumedev.seijakulistkmp.core.util

interface MediaStringFormatter {
    suspend fun formatMediaFormat(format: String?): String?
    suspend fun formatEpisodes(episodes: Int?): String?
    suspend fun formatChapters(chapters: Int?): String?
    suspend fun formatVolumes(volumes: Int?): String?

    fun formatRating(averageScore: Int?): String?

    suspend fun formatStatus(status: String?, isManga: Boolean = false): String?

    suspend fun buildAnimeMetadata(
        seasonYear: Int?,
        format: String?,
        episodes: Int?
    ): String

    suspend fun buildMangaMetadata(
        startYear: Int?,
        format: String?,
        chapters: Int?,
        volumes: Int?
    ): String

    suspend fun getUnknownString(): String
}
