package com.yumedev.seijakulistkmp.core.util

import org.jetbrains.compose.resources.getString
import seijakulistkmp.shared.generated.resources.*

class MediaStringFormatterImpl : MediaStringFormatter {
    override suspend fun formatMediaFormat(format: String?): String? {
        return when (format) {
            "TV" -> getString(Res.string.format_tv)
            "TV_SHORT" -> getString(Res.string.format_tv_short)
            "MOVIE" -> getString(Res.string.format_movie)
            "SPECIAL" -> getString(Res.string.format_special)
            "OVA" -> getString(Res.string.format_ova)
            "ONA" -> getString(Res.string.format_ona)
            "MUSIC" -> getString(Res.string.format_music)
            "MANGA" -> getString(Res.string.format_manga)
            "LIGHT_NOVEL" -> getString(Res.string.format_light_novel)
            "ONE_SHOT" -> getString(Res.string.format_one_shot)
            else -> null
        }
    }

    override suspend fun formatEpisodes(episodes: Int?): String? {
        return episodes?.let {
            "$it ${getString(Res.string.episodes_suffix)}"
        }
    }

    override suspend fun formatChapters(chapters: Int?): String? {
        return chapters?.let {
            "$it Ch"
        }
    }

    override suspend fun formatVolumes(volumes: Int?): String? {
        return volumes?.let {
            "$it Vol"
        }
    }

    override fun formatRating(averageScore: Int?): String? {
        return averageScore?.let { score ->
            val rating = score / 10.0
            String.format("%.1f", rating)
        }
    }

    override suspend fun formatStatus(status: String?, isManga: Boolean): String? {
        return when (status) {
            "RELEASING" -> if (isManga) getString(Res.string.status_publishing) else getString(Res.string.status_airing)
            "FINISHED" -> getString(Res.string.status_finished)
            "NOT_YET_RELEASED" -> if (isManga) getString(Res.string.status_not_yet_released) else getString(Res.string.status_not_yet_aired)
            "CANCELLED" -> getString(Res.string.status_cancelled)
            "HIATUS" -> getString(Res.string.status_hiatus)
            else -> null
        }
    }

    override suspend fun buildAnimeMetadata(
        seasonYear: Int?,
        format: String?,
        episodes: Int?
    ): String {
        return buildList {
            seasonYear?.let { add(it.toString()) }
            formatMediaFormat(format)?.let { add(it) }
            formatEpisodes(episodes)?.let { add(it) }
        }.joinToString(" · ")
    }

    override suspend fun buildMangaMetadata(
        startYear: Int?,
        format: String?,
        chapters: Int?,
        volumes: Int?
    ): String {
        return buildList {
            startYear?.let { add(it.toString()) }
            formatMediaFormat(format)?.let { add(it) }
            chapters?.let { add("$it Ch") }
            volumes?.let { add("$it Vol") }
        }.joinToString(" · ")
    }

    override suspend fun getUnknownString(): String {
        return getString(Res.string.unknown)
    }
}
