package com.yumedev.seijakulistkmp.features.profile.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.yumedev.seijakulistkmp.features.profile.domain.model.MediaStatistics
import com.yumedev.seijakulistkmp.features.profile.domain.model.ProfileImage
import com.yumedev.seijakulistkmp.features.profile.domain.model.TitleLanguage
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserOptions
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserStatistics
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val anilistId: Int? = null,
    val name: String,
    val avatarLarge: String? = null,
    val avatarMedium: String? = null,
    val banner: String? = null,
    val about: String? = null,
    val animeCount: Int = 0,
    val animeMeanScore: Double = 0.0,
    val minutesWatched: Int = 0,
    val episodesWatched: Int = 0,
    val mangaCount: Int = 0,
    val mangaMeanScore: Double = 0.0,
    val chaptersRead: Int = 0,
    val volumesRead: Int = 0,
    val displayAdultContent: Boolean = false,
    val titleLanguage: String = TitleLanguage.ROMAJI.name,
    val isLocal: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long,
) {
    fun toDomain(): UserProfile {
        return UserProfile(
            id = id,
            anilistId = anilistId,
            name = name,
            avatar = if (avatarLarge != null || avatarMedium != null) {
                ProfileImage(large = avatarLarge, medium = avatarMedium)
            } else {
                null
            },
            banner = banner,
            about = about,
            statistics = UserStatistics(
                anime = MediaStatistics(
                    count = animeCount,
                    meanScore = animeMeanScore,
                    minutesWatched = minutesWatched,
                    episodesWatched = episodesWatched,
                ),
                manga = MediaStatistics(
                    count = mangaCount,
                    meanScore = mangaMeanScore,
                    chaptersRead = chaptersRead,
                    volumesRead = volumesRead,
                ),
            ),
            options = UserOptions(
                displayAdultContent = displayAdultContent,
                titleLanguage = TitleLanguage.valueOf(titleLanguage),
            ),
            isLocal = isLocal,
            createdAt = Instant.fromEpochMilliseconds(createdAt),
            updatedAt = Instant.fromEpochMilliseconds(updatedAt),
        )
    }

    companion object {
        fun fromDomain(profile: UserProfile): UserProfileEntity {
            return UserProfileEntity(
                id = profile.id,
                anilistId = profile.anilistId,
                name = profile.name,
                avatarLarge = profile.avatar?.large,
                avatarMedium = profile.avatar?.medium,
                banner = profile.banner,
                about = profile.about,
                animeCount = profile.statistics.anime.count,
                animeMeanScore = profile.statistics.anime.meanScore,
                minutesWatched = profile.statistics.anime.minutesWatched,
                episodesWatched = profile.statistics.anime.episodesWatched,
                mangaCount = profile.statistics.manga.count,
                mangaMeanScore = profile.statistics.manga.meanScore,
                chaptersRead = profile.statistics.manga.chaptersRead,
                volumesRead = profile.statistics.manga.volumesRead,
                displayAdultContent = profile.options.displayAdultContent,
                titleLanguage = profile.options.titleLanguage.name,
                isLocal = profile.isLocal,
                createdAt = profile.createdAt.toEpochMilliseconds(),
                updatedAt = profile.updatedAt.toEpochMilliseconds(),
            )
        }

        fun createDefault(name: String = "Usuario Local"): UserProfileEntity {
            val now = Clock.System.now().toEpochMilliseconds()
            return UserProfileEntity(
                id = 1,
                anilistId = null,
                name = name,
                isLocal = true,
                createdAt = now,
                updatedAt = now,
            )
        }
    }
}
