package com.yumedev.seijakulistkmp.features.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val uid: String = "",
    val email: String? = null,
    val name: String = "",
    val avatar: ProfileImageDto? = null,
    val banner: String? = null,
    val about: String? = null,
    val statistics: UserStatisticsDto = UserStatisticsDto(),
    val options: UserOptionsDto = UserOptionsDto(),
    val anilistId: Int? = null,
    val isLocal: Boolean = true,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
)

@Serializable
data class ProfileImageDto(
    val large: String? = null,
    val medium: String? = null,
)

@Serializable
data class UserStatisticsDto(
    val anime: MediaStatisticsDto = MediaStatisticsDto(),
    val manga: MediaStatisticsDto = MediaStatisticsDto(),
)

@Serializable
data class MediaStatisticsDto(
    val count: Int = 0,
    val meanScore: Double = 0.0,
    val minutesWatched: Int = 0,
    val episodesWatched: Int = 0,
    val chaptersRead: Int = 0,
    val volumesRead: Int = 0,
)

@Serializable
data class UserOptionsDto(
    val displayAdultContent: Boolean = false,
    val titleLanguage: String = "ROMAJI",
)
