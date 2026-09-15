package com.yumedev.seijakulistkmp.features.profile.data.remote.mapper

import com.yumedev.seijakulistkmp.features.profile.data.remote.dto.*
import com.yumedev.seijakulistkmp.features.profile.domain.model.*
import kotlinx.datetime.Instant

fun UserProfile.toDto(firebaseUid: String): UserProfileDto {
    return UserProfileDto(
        uid = firebaseUid,
        email = null,
        name = this.name,
        avatar = this.avatar?.toDto(),
        banner = this.banner,
        about = this.about,
        statistics = this.statistics.toDto(),
        options = this.options.toDto(),
        anilistId = this.anilistId,
        isLocal = this.isLocal,
        createdAt = this.createdAt.toEpochMilliseconds(),
        updatedAt = this.updatedAt.toEpochMilliseconds(),
    )
}

fun UserProfileDto.toDomain(localId: Int): UserProfile {
    return UserProfile(
        id = localId,
        anilistId = this.anilistId,
        name = this.name,
        avatar = this.avatar?.toDomain(),
        banner = this.banner,
        about = this.about,
        statistics = this.statistics.toDomain(),
        options = this.options.toDomain(),
        isLocal = this.isLocal,
        createdAt = Instant.fromEpochMilliseconds(this.createdAt),
        updatedAt = Instant.fromEpochMilliseconds(this.updatedAt),
    )
}

fun ProfileImage.toDto() = ProfileImageDto(
    large = this.large,
    medium = this.medium,
)

fun ProfileImageDto.toDomain() = ProfileImage(
    large = this.large,
    medium = this.medium,
)

fun UserStatistics.toDto() = UserStatisticsDto(
    anime = this.anime.toDto(),
    manga = this.manga.toDto(),
)

fun UserStatisticsDto.toDomain() = UserStatistics(
    anime = this.anime.toDomain(),
    manga = this.manga.toDomain(),
)

fun MediaStatistics.toDto() = MediaStatisticsDto(
    count = this.count,
    meanScore = this.meanScore,
    minutesWatched = this.minutesWatched,
    episodesWatched = this.episodesWatched,
    chaptersRead = this.chaptersRead,
    volumesRead = this.volumesRead,
)

fun MediaStatisticsDto.toDomain() = MediaStatistics(
    count = this.count,
    meanScore = this.meanScore,
    minutesWatched = this.minutesWatched,
    episodesWatched = this.episodesWatched,
    chaptersRead = this.chaptersRead,
    volumesRead = this.volumesRead,
)

fun UserOptions.toDto() = UserOptionsDto(
    displayAdultContent = this.displayAdultContent,
    titleLanguage = this.titleLanguage.name,
)

fun UserOptionsDto.toDomain() = UserOptions(
    displayAdultContent = this.displayAdultContent,
    titleLanguage = try {
        TitleLanguage.valueOf(this.titleLanguage)
    } catch (e: IllegalArgumentException) {
        TitleLanguage.ROMAJI
    },
)
