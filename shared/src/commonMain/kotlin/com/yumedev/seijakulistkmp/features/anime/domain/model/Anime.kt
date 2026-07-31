package com.yumedev.seijakulistkmp.features.anime.domain.model

/**
 * Domain model representing an Anime
 */
data class Anime(
    val id: Int,
    val title: AnimeTitle,
    val coverImage: AnimeCoverImage,
    val format: AnimeFormat?,
    val status: AnimeStatus?,
    val episodes: Int?,
    val season: AnimeSeason?,
    val seasonYear: Int?,
    val averageScore: Int?,
    val popularity: Int?,
    val genres: List<String>,
    val description: String?,
    val bannerImage: String?,
    val isAdult: Boolean
)

/**
 * Anime title in different languages
 */
data class AnimeTitle(
    val romaji: String?,
    val english: String?,
    val native: String?
) {
    val displayTitle: String
        get() = english ?: romaji ?: native ?: "Unknown"
}

/**
 * Anime cover image URLs
 */
data class AnimeCoverImage(
    val extraLarge: String?,
    val large: String?,
    val medium: String?,
    val color: String?
) {
    val bestQuality: String?
        get() = extraLarge ?: large ?: medium
}

/**
 * Anime format types
 */
enum class AnimeFormat {
    TV,
    TV_SHORT,
    MOVIE,
    SPECIAL,
    OVA,
    ONA,
    MUSIC,
    MANGA,
    NOVEL,
    ONE_SHOT;

    companion object {
        fun fromString(value: String?): AnimeFormat? = try {
            value?.let { valueOf(it) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}

/**
 * Anime release status
 */
enum class AnimeStatus {
    FINISHED,
    RELEASING,
    NOT_YET_RELEASED,
    CANCELLED,
    HIATUS;

    companion object {
        fun fromString(value: String?): AnimeStatus? = try {
            value?.let { valueOf(it) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}

/**
 * Anime season
 */
enum class AnimeSeason {
    WINTER,
    SPRING,
    SUMMER,
    FALL;

    companion object {
        fun fromString(value: String?): AnimeSeason? = try {
            value?.let { valueOf(it) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
