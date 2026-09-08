package com.yumedev.seijakulistkmp.features.profile.domain.model

import kotlinx.datetime.Instant

data class UserProfile(
    val id: Int,
    val anilistId: Int? = null,
    val name: String,
    val avatar: ProfileImage? = null,
    val banner: String? = null,
    val about: String? = null,
    val statistics: UserStatistics = UserStatistics(),
    val options: UserOptions = UserOptions(),
    val isLocal: Boolean = true,
    val createdAt: Instant,
    val updatedAt: Instant,
)

data class ProfileImage(
    val large: String? = null,
    val medium: String? = null,
)

data class UserStatistics(
    val anime: MediaStatistics = MediaStatistics(),
    val manga: MediaStatistics = MediaStatistics(),
)

data class MediaStatistics(
    val count: Int = 0,
    val meanScore: Double = 0.0,
    val minutesWatched: Int = 0,
    val episodesWatched: Int = 0,
    val chaptersRead: Int = 0,
    val volumesRead: Int = 0,
)

data class UserOptions(
    val displayAdultContent: Boolean = false,
    val titleLanguage: TitleLanguage = TitleLanguage.ROMAJI,
)

enum class TitleLanguage {
    ROMAJI,
    ENGLISH,
    NATIVE,
    ROMAJI_STYLISED,
    ENGLISH_STYLISED,
    NATIVE_STYLISED
}
